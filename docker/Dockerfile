FROM ubuntu:14.10
ADD ./zookeeper.pp /opt/pp/
RUN apt-get update && \
    apt-get install -y puppet ca-certificates && \
    puppet module install deric-zookeeper && \
    puppet apply /opt/pp/zookeeper.pp

#/usr/share/zookeeper/bin/zkServer.sh
CMD ["/usr/share/zookeeper/bin/zkServer.sh", "start-foreground"]
