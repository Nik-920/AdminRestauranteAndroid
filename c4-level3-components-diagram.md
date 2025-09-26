# Diagrama C4 Nivel 3 - Componentes
## Sistema AdminRestauranteAndroid

```mermaid
C4Context
    title Component diagram for AdminRestaurante Android System
    
    Container_Boundary(android_app, "AdminRestaurante Android Application") {
        
        Component_Boundary(ui_layer, "UI Layer") {
            Component(login_ui, "Login/Register UI", "Activities", "Maneja autenticación de usuarios y registro")
            Component(splash_ui, "Splash Screen UI", "Activities", "Pantallas de bienvenida y navegación inicial")
            Component(navigation_ui, "Navigation UI", "Activities", "Navegación principal del admin")
            Component(categories_ui, "Categories UI", "Activities/Adapters", "Gestión de categorías de platillos")
            Component(menu_ui, "Menu Management UI", "Activities/Adapters", "Gestión de platillos y menús")
            Component(orders_ui, "Orders Management UI", "Activities/Adapters", "Gestión de pedidos y estados")
            Component(users_ui, "Users Management UI", "Activities/Adapters", "Administración de usuarios")
            Component(billing_ui, "Billing UI", "Activities/Adapters", "Caja y procesamiento de pagos") 
            Component(account_ui, "Account Management UI", "Activities", "Gestión de cuentas de mesa")
        }
        
        Component_Boundary(business_layer, "Business Logic Layer") {
            Component(auth_logic, "Authentication Logic", "Utils/Logic", "Validación de credenciales y sesiones")
            Component(order_logic, "Order Management Logic", "Utils/Logic", "Lógica de pedidos y estados")
            Component(account_logic, "Account Logic", "Utils", "Gestión de cuentas, totales y mesas")
            Component(data_validation, "Data Validation", "Utils", "Validación de datos y reglas de negocio")
        }
        
        Component_Boundary(data_layer, "Data Layer") {
            Component(models, "Data Models", "Models", "Usuario, Pedido, Platillo, Categoria, DetallePedido")
            Component(network_client, "Network Client", "RetrofitClient", "Cliente HTTP para comunicación con API")
            Component(web_service, "Web Service Interface", "WebService", "Definición de endpoints de API REST")
            Component(local_storage, "Local Storage", "SharedPreferences", "Almacenamiento local de sesión y carrito")
        }
        
        Component_Boundary(network_layer, "Network Layer") {
            Component(response_handlers, "Response Handlers", "Response Classes", "Manejo de respuestas de API")
            Component(request_builders, "Request Builders", "Network Utils", "Construcción de requests HTTP")
        }
    }
    
    System_Ext(backend_api, "Backend API REST", "API del servidor del restaurante para gestión de datos")
    SystemDb_Ext(database, "Restaurant Database", "Base de datos del restaurante con usuarios, platillos, pedidos")
    
    ' UI Layer relationships
    Rel(login_ui, auth_logic, "Uses", "Validación")
    Rel(splash_ui, navigation_ui, "Redirects to", "Navigation")
    Rel(categories_ui, order_logic, "Uses", "Category management")
    Rel(menu_ui, order_logic, "Uses", "Menu management")
    Rel(orders_ui, order_logic, "Uses", "Order processing")
    Rel(users_ui, auth_logic, "Uses", "User management")
    Rel(billing_ui, account_logic, "Uses", "Payment processing")
    Rel(account_ui, account_logic, "Uses", "Account management")
    
    ' Business Logic relationships
    Rel(auth_logic, network_client, "Uses", "API calls")
    Rel(order_logic, network_client, "Uses", "API calls")
    Rel(account_logic, local_storage, "Uses", "Session data")
    Rel(data_validation, models, "Uses", "Validation")
    
    ' Data Layer relationships
    Rel(network_client, web_service, "Implements", "API interface")
    Rel(web_service, response_handlers, "Uses", "Response mapping")
    Rel(network_client, request_builders, "Uses", "Request construction")
    Rel(models, response_handlers, "Used by", "Data mapping")
    
    ' External relationships
    Rel(web_service, backend_api, "Makes API calls", "HTTPS/REST")
    Rel(backend_api, database, "Reads/Writes", "SQL")
    
    ' Data flow relationships
    BiRel(local_storage, account_logic, "Store/Retrieve", "Session & Cart data")
    BiRel(response_handlers, models, "Maps to/from", "JSON/Object mapping")
```

## Descripción de Componentes

### UI Layer (Capa de Interfaz de Usuario)
- **Login/Register UI**: Maneja la autenticación de usuarios (LoginActivity, RegisterActivity)
- **Splash Screen UI**: Pantallas de bienvenida (SplashScreenActivity, SplashScreen1Activity)
- **Navigation UI**: Navegación principal (NavegacionActivity)
- **Categories UI**: Gestión de categorías (CategoriasActivity, CategoriasActivityUsu, AdaptadorCategoria)
- **Menu Management UI**: Gestión de platillos (PlatillosActivity, PlatillosActivityUsu, AdaptadorPlatillo)
- **Orders Management UI**: Gestión de pedidos (PedidosActivityAdmin, AdaptadorPedidosAdmin)
- **Users Management UI**: Administración de usuarios (UsuariosActivityAdmin, AdaptadorUsuarioAdmin)
- **Billing UI**: Caja y pagos (CajaPagoActivity, AdaptadorCaja)
- **Account Management UI**: Gestión de cuentas de mesa (CuentasActivity)

### Business Logic Layer (Capa de Lógica de Negocio)
- **Authentication Logic**: Validación de credenciales y manejo de sesiones
- **Order Management Logic**: Lógica de pedidos, estados y procesamiento
- **Account Logic**: Gestión de cuentas, totales y asignación de mesas (Utils)
- **Data Validation**: Validación de datos y reglas de negocio

### Data Layer (Capa de Datos)
- **Data Models**: Modelos de datos (Usuario, Pedido, Platillo, Categoria, DetallePedido)
- **Network Client**: Cliente HTTP (RetrofitClient)
- **Web Service Interface**: Definición de endpoints (WebService)
- **Local Storage**: Almacenamiento local (SharedPreferences)

### Network Layer (Capa de Red)
- **Response Handlers**: Clases de respuesta de API
- **Request Builders**: Construcción de requests HTTP

## Sistemas Externos
- **Backend API REST**: API del servidor del restaurante
- **Restaurant Database**: Base de datos del restaurante