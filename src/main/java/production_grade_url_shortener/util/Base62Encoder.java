package production_grade_url_shortener.util;

import org.springframework.stereotype.Component;

@Component
public class Base62Encoder {
  
    private static final String charset = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = 62;
    public  String encode(long value)
    {
        if(value <0)
        {
            throw new IllegalArgumentException("value must be greater than 0");
        }
        if(value==0)
            return "0";
        StringBuilder builder = new StringBuilder();
        long temp = value;
        while(temp!=0)
        {
            int rem=(int)(temp%BASE);
            temp=temp/BASE;
            builder.append(charset.charAt(rem));
        }
        return builder.reverse().toString();
    }

    public long decode(String value)
    {
        if(value == null || value.isEmpty())
        {
            throw new IllegalArgumentException("value must not be null or empty");
        }
        long result = 0;
        for (char c : value.toCharArray()) {

            int index = charset.indexOf(c);

            if (index == -1) {
                throw new IllegalArgumentException("Invalid Base62 character: " + c);
            }

            result = result * BASE + index;
        }

    return result;
    }
}