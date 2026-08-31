Run IBM MQ via Docker:
docker run -d --name ibm-mq \
  -p 1414:1414 \
  -p 9443:9443 \
  -e LICENSE=accept \
  -e MQ_QMGR_NAME=QM1 \
  ibmcom/mq:latest

Verify it's running

docker ps | grep ibm-mq
docker logs ibm-mq

Access MQ Web Console:
https://<your-ec2-public-ip>:9443/ibmmq/console
https://13.201.100.105:9443/ibmmq/console

Field		Value
Username	admin
Password	passw0rd

Default objects already created inside the container
Object			Name
Queue Manager	QM1
Channel			DEV.APP.SVRCONN
Test Queue		DEV.QUEUE.1
