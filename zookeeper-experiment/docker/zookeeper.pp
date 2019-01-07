class { 'zookeeper': 
  install_java => true,
  java_package => 'openjdk-7-jre-headless',
  client_ip => '0.0.0.0'
}
