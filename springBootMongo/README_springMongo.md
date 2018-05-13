# mongodbTutorial
personal mongodb learning note

http://127.0.0.1:8080/swagger-ui.html

java driver website http://mongodb.github.io/mongo-java-driver/
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
然后执行db.createUser({user: "user1", pwd: "passwd1", roles: [{role: "readWrite", db: "db1"}]}


mongo -u "user1" -p "passwd1" --authenticationDatabase "db1"
生成还有main class的jar
mvn assembly:assembly


备份数据 mongodump  -u "user1" -p "passwd1" -d "db1"
root@ubuntu2:/opt# mongodump  -u "user1" -p "passwd1" -d "db1"
2018-03-09T15:24:05.648+0800    writing db1.person to
2018-03-09T15:24:05.648+0800    writing db1.person2 to
2018-03-09T15:24:05.649+0800    writing db1.customer to
2018-03-09T15:24:05.649+0800    writing db1.col1 to
2018-03-09T15:24:05.650+0800    done dumping db1.person (10 documents)
2018-03-09T15:24:05.650+0800    done dumping db1.person2 (9 documents)
2018-03-09T15:24:05.657+0800    done dumping db1.customer (3 documents)
2018-03-09T15:24:05.658+0800    done dumping db1.col1 (3 documents)
默认情况下，MongoDB 会在当前目录下创建一个 dump 目录，并把所有的数据库按数据库名称创建目录。
root@ubuntu2:/opt/dump/db1# ls -al
total 40
drwxr-xr-x 2 root root 4096 Mar  9 15:24 .
drwxr-xr-x 3 root root 4096 Mar  9 15:24 ..
-rw-r--r-- 1 root root  993 Mar  9 15:24 col1.bson
-rw-r--r-- 1 root root   80 Mar  9 15:24 col1.metadata.json
-rw-r--r-- 1 root root  287 Mar  9 15:24 customer.bson
-rw-r--r-- 1 root root   84 Mar  9 15:24 customer.metadata.json
-rw-r--r-- 1 root root  567 Mar  9 15:24 person2.bson
-rw-r--r-- 1 root root   83 Mar  9 15:24 person2.metadata.json
-rw-r--r-- 1 root root  670 Mar  9 15:24 person.bson
-rw-r--r-- 1 root root   82 Mar  9 15:24 person.metadata.json
root@ubuntu2:/opt/dump/db1# tree

