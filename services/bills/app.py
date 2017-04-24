from fr.sogeti.consul.config_resolver import ConfigResolver
from fr.sogeti.consul.service_discovery import ServiceDiscovery
import threading
import os


def deregister(discovery, ids):
    [discovery.deregister(id) for id in ids]
    print("services unregistred")

if "__main__" == __name__:
    import fr.sogeti.rest as rest
    consul_client = os.environ.get('CONSUL_CLIENT')
    if consul_client is None:
        print("CONSUL_CLIENT environment variable not set properly")
        exit(1)

    discovery = ServiceDiscovery(consul_client)
    deregister(discovery, ['bills-http', 'bills-db'])

    consul = ConfigResolver(consul_client)
    consul.get_address("eth0")
    config = consul.get_config("config/services/bills")

    address = consul.get_address(config.interface)
    discovery.register('bills-http', 'bills-service', address, config.port, 'http', ('service', 'bills'), route='/api/v1/check')
    discovery.register('bills-db', 'bills-service', config.db_host, config.db_port, 'tcp', ('database', 'bills'))

    thread = threading.Thread(target=rest.run, args=[address, config.port])
    thread.start()
    print("running on %s" % address)
