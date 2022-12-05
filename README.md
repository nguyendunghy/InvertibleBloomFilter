# InvirtableBloomFilter

- Note: Teradata does not support Mac M1
# Install teradata, vmware on window 
- Follow every step in website: https://quickstarts.teradata.com/getting.started.vmware.html
- Note
    * Change vmware network adapter to NAT
    * Run command ifconfig, get the ip address to set up the host in connection

# Config and run project
- Copy libs/terajdbc-4.jar to folder ~/.m2/repository/com/teradata/jdbc/terajdbc/4
- Run file init.sql then users.sql in tera studio
- Run project 