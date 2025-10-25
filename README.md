# PlaceholderAPI 实用占位符工具
### 需要将该插件安装至 `plugins\PlaceholderAPI\expansions` 目录下。

---
<br>

# 可用占位符
- 时间计算（当天/次日）
  1. 可用单位: `milli, second, minute, hour, day, month, year`
  2. %mut_diffDays.milli."HH:mm:ss".true%
  3. %mut_diffDays.second."HH:mm:ss".false%

-  计算到下个星期几
  1. 可用单位: `milli, second, minute, hour, day, month, year`
  2. %mut_diffWeeks.minute."HH:mm:ss".5%

- 计算到下个月指定日期
  1. 可用单位: `milli, second, minute, hour, day, month, year`
  2. %mut_diffMonths.hour."HH:mm:ss".15%

-  AuthMe 插件相关
  1. %mut_authMe.registrationDate."yyyy-MM-dd HH:mm:ss"% # 获取注册的时间
  2. %mut_authMe.registrationDiffDate.day%               # 获取注册到现在的时间单位
  3. %mut_authMe.listNameByIp.","%                       # 获取已登录账号列表
  4. %mut_authMe.getUserCountByIp%                       # 获取已登录账户数量
  5. %mut_authMe.registered%                             # 是否已注册

- Bukkit API
  1. %mut_bukkit.emptySlots% # 背包空格(不包括装备栏和副手)

---