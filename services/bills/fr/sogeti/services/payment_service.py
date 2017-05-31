from bson.json_util import dumps, loads
from http.client import HTTPConnection

from bson.objectid import ObjectId

from fr.sogeti.entities.bill import Bill


class PaymentService:

    def __init__(self, bills_service):
        self.route_products = '/api/v1/products/%d'
        self.route_carts = '/api/v1/carts/%s'
        self.route_customers = '/api/v1/customers/%s'
        self.route_suppliers = '/api/v1/suppliers/%d'
        self.bills_service = bills_service

    def create(self, id_user, gateway_url):
        print("requested user's bills : %s" % id_user)
        cart = self._resolve_cart_products(gateway_url, id_user)
        products = [self._resolve_get(gateway_url, self.route_products, id) for id, quantity in cart]
        for i in range(len(products)):
            products[i]['quantity'] = cart[i][1]
        suppliers = self._product_to_dict_suppliers(products)
        bills = []
        customer = self._resolve_get(gateway_url, self.route_customers, id_user)
        for id_supplier in suppliers.keys():
            supplier_products = suppliers[id_supplier]
            total = sum([p['price'] for p in supplier_products])
            supplier = self._resolve_get(gateway_url, self.route_suppliers, id_supplier)
            for needed, given in [('company_name', 'company'), ('email', 'mail'), ('phone_number', 'phone')]:
                supplier[needed] = supplier[given]
            suppliers_products = [product for product in products if product['idSupplier'] == id_supplier]
            bill = Bill(None, suppliers_products, supplier, customer, total)
            bills.append(bill)

        created_ids = [str(self.bills_service.create(bill)) for bill in bills]
        created_products = [self.bills_service.get_by_id(id_product) for id_product in created_ids]
        self._delete_cart(gateway_url, self.route_carts, id_user)
        return dumps(created_products)

    def _product_to_dict_suppliers(self, products):
        suppliers = {}
        for product in products:
            id_supplier = product['idSupplier']
            if id_supplier not in suppliers.keys():
                suppliers[id_supplier] = []
            suppliers[id_supplier].append(product)
        return suppliers

    def _resolve_cart_products(self, gateway_url, id_user):
        co = HTTPConnection(gateway_url)
        co.request("GET", self.route_carts % id_user)
        response = co.getresponse()
        data = self.decode_data(response.read())
        data = loads(data)
        if not isinstance(data, dict) or 'cartElements' not in data.keys():
            print("unable to find a cart")
            return []
        return [(p['productID'], p['quantity']) for p in data['cartElements']]

    def _resolve_get(self, gateway_url, route, id_obj):
        co = HTTPConnection(gateway_url)
        co.request("GET", route % id_obj)
        response = co.getresponse()
        response = self.decode_data(response.read())
        print("request on %s \n data received : %s" % (route, response))
        return loads(response)

    def _delete_cart(self, gateway_url, route, id_user):
        co = HTTPConnection(gateway_url)
        co.request("DELETE", route % id_user)
        return co.getresponse()

    def decode_data(self, data):
        if type(data) == type(b''):
            data = data.decode('utf-8')
        return data