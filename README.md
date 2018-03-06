# mongodbTutorial
personal mongodb learning note

在非认证模式的时候，添加了如下用户
use admin
db.createUser(
  {
    user: "myUserAdmin",
    pwd: "abc123",
    roles: [ { role: "userAdminAnyDatabase", db: "admin" } ]
  }
)

重新启动mongodb
mongod -f /etc/mongod.conf --fork --auth 

启用认证后这样可以进入mongo shell
mongo -u "myUserAdmin" -p "abc123" --authenticationDatabase "admin"

针对db1
在启用auth之前，在mongo shell下面
先switch到 db1 数据库。 命令use db1
然后执行db.createUser({user: "user1", pwd: "passwd1", roles: [{role: "readWrite", db: "db1"}]})