-- KEYS[1]: Rate limit key (e.g. "rate:apikey:<hash>" or "rate:ip:<ip>")
-- ARGV[1]: Capacity (maximum tokens, e.g. 10.0)
-- ARGV[2]: Refill rate per millisecond (e.g. tokens_per_sec / 1000.0)
-- ARGV[3]: Requested tokens / cost (e.g. 1.0)
-- ARGV[4]: Current epoch time in milliseconds
-- ARGV[5]: TTL for the key in seconds to prevent stale memory leaks

local key = KEYS[1]
local capacity = ARGV[1]
local refill_rate = ARGV[2]
local cost = ARGV[3]
local now = tonumber(ARGV[4])
local ttl =tonumber(ARGV[5])

local data = redis.call("HMGET", key, "tokens", "last_refereshed")
local curr_tokens = tonumber(data[1])
local last_refreshed = tonumber(data[2])

if curr_tokens == nil then
    curr_tokens = capacity
    last_refreshed = now
else
    local elapsed_time = math.max(now - last_refreshed, 0)
    local refill_tokens = elapsed_time * refill_rate
    curr_tokens = math.min(curr_tokens + refill_tokens, capacity)
    last_refreshed = now
end

if curr_tokens >= cost then
    curr_tokens = curr_tokens - cost
    redis.call("HSET", key, "tokens", curr_tokens, "last_refereshed", last_refreshed)
    redis.call("EXPIRE", key, ttl)
    return {1, math.floor(current_tokens)}
else
    redis.call("HSET", key, "tokens", curr_tokens, "last_refereshed", last_refreshed)
    redis.call("EXPIRE", key, ttl)
    return {0, math.floor(current_tokens)}
end
