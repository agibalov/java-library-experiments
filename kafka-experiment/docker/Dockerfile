FROM ubuntu:14.10
ADD ./kafka.pp /opt/pp/
ADD ./server.properties /opt/kafka-cfg/
RUN apt-get update && \
    apt-get install -y puppet ca-certificates && \
    puppet module install puppetlabs-java && \
    puppet module install puppet-kafka && \
    puppet apply /opt/pp/kafka.pp

CMD /opt/kafka/bin/zookeeper-server-start.sh /opt/kafka/config/zookeeper.properties & \
    /opt/kafka/bin/kafka-server-start.sh /opt/kafka-cfg/server.properties
