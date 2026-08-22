package production_grade_url_shortener.util;

public class SnowFlakeIdgenerator implements IdGenerator {
    
    private static final long CUSTOM_EPOCH = 1735689600000L; // 1 Jan 2025

    private static final int WORKER_ID_BITS = 10;
    private static final int SEQUENCE_BITS = 12;
    private static final long MAX_WORKER_ID = (1L << WORKER_ID_BITS) - 1;
    private static final long MAX_SEQUENCE = (1L << SEQUENCE_BITS) - 1;

    private static final int WORKER_ID_SHIFT = SEQUENCE_BITS;
    private static final int TIMESTAMP_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

    private final long workerId;
    private long sequence =0L;
    private long lastTimestamp = -1L;

    public SnowFlakeIdgenerator(long workerId) 
        {
            if(workerId<0 || workerId> MAX_WORKER_ID)
                throw new IllegalArgumentException("workerId must be between 0 and " + MAX_WORKER_ID);
            this.workerId = workerId;
        }
  
    @Override
    public synchronized long generateId()
        {
            long curr_timeStamp = currentTimeStamp();
            if(curr_timeStamp < lastTimestamp)
            {
                throw new IllegalStateException("Clock moved backwards.");
            }
            if(curr_timeStamp == lastTimestamp)
            {
                sequence = (sequence + 1) & MAX_SEQUENCE;
                if(sequence == 0)
                    curr_timeStamp = waitTillNextMillis(lastTimestamp);
            }
            else
            {
                    sequence = 0;
             }
            lastTimestamp = curr_timeStamp;
            return ((curr_timeStamp - CUSTOM_EPOCH) << TIMESTAMP_SHIFT) | (workerId << WORKER_ID_SHIFT) | sequence;
        }
    private long currentTimeStamp()
    {
        return System.currentTimeMillis();
    }
    private long waitTillNextMillis(long lastTimestamp)
    {
        long timestamp = currentTimeStamp();

        while (timestamp <= lastTimestamp) {
            timestamp = currentTimeStamp();
        }

        return timestamp;
    }
}

