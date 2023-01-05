-- 参数列表
local couponId = ARGV[1] -- 优惠券id
local userId = ARGV[2] -- 用户id
-- 数据key
local couponStockKey = "coupon:stock:" .. couponId; -- 库存key
local CouponOrderKey = "coupon:order:" .. couponId .. ":" .. userId; -- 订单key
-- 判断库存是否充足
if (tonumber(redis.call('get', couponStockKey)) <= 0) then
    return -1;
end
-- 判断用户是否下单
if (redis.call('sismember', CouponOrderKey, userId) == 1) then
    return -2;
end
-- 扣减库存
redis.call('decrby', couponStockKey, 1)
-- 用户下单
redis.call('sadd', CouponOrderKey, userId)
return 1;

