# AdminRestaurante Android

Sistema de administración de restaurante desarrollado en Android con Kotlin.

## Arquitectura

Este proyecto sigue una arquitectura por capas organizada de la siguiente manera:

- **UI Layer**: Activities, Fragments y Adapters para la interfaz de usuario
- **Business Logic Layer**: Lógica de negocio y validaciones
- **Data Layer**: Modelos de datos, cliente de red y almacenamiento local
- **Network Layer**: Manejo de requests y responses HTTP

## Documentación

- [Diagrama C4 Nivel 3 - Componentes](c4-level3-components-diagram.md)

## Características

- Gestión de usuarios y autenticación
- Administración de categorías y platillos
- Gestión de pedidos y estados
- Sistema de caja y pagos
- Gestión de cuentas por mesa

## Tecnologías

- **Kotlin** - Lenguaje de programación
- **Android SDK** - Framework móvil
- **Retrofit** - Cliente HTTP
- **Gson** - Serialización JSON
- **Glide** - Carga de imágenes
- **Toasty** - Notificaciones mejoradas