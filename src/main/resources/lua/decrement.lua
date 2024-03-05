
local number = tonumber(redis.call("GET", KEYS[1]))

if 0 >= number then
    return false
end

local decreasedNumber = tonumber(redis.call("DECR", KEYS[1]))

return true