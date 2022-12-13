-- 验证当前线程标识和锁的中的线程标识是否一致
if (ARGV[1] == redis.call("get",  KEYS[1])) then
    -- 是否锁
    return redis.call('del', KEYS[1])
end
return 0
