# CotizacionApp (Frontend con Autenticación JWT)

Este es el frontend del sistema de cotizaciones, desarrollado en **Angular 21** y estilizado con **Bootstrap**.  
Incluye una integración completa con el backend seguro para inicio de sesión, almacenamiento local del token y envío automático de credenciales en cada petición HTTP.

---

## Características de Autenticación Implementadas

- **`LoginComponent`**: Pantalla de Login responsiva e interactiva con diseño de tarjetas premium, animaciones de entrada, validación reactiva de campos y gestión dinámica de errores.
- **`AuthService`**: Servicio centralizado que gestiona el estado de sesión usando **Angular Signals** (`isLoggedIn`, `currentUser`, `roles`, `nombrePersona`). El estado persiste de manera local en el `localStorage`.
- **`AuthGuard`**: Guard de ruta que protege los módulos de negocio (Categorías, Clientes, Cotizaciones, etc.) de accesos no autorizados, redirigiendo al `/login`.
- **`AuthInterceptor`**: Interceptor HTTP funcional que inyecta automáticamente el token JWT como `Authorization: Bearer <token>` en cada consulta al backend.

---

## Estructura del Código de Autenticación

- **Servicios:** [auth.service.ts](file:///d:/Proyectos/Pumita/03_Frontend/cotizacion-app/src/app/core/services/auth.service.ts)
- **Interceptors:** [auth.interceptor.ts](file:///d:/Proyectos/Pumita/03_Frontend/cotizacion-app/src/app/core/interceptors/auth.interceptor.ts)
- **Guards:** [auth.guard.ts](file:///d:/Proyectos/Pumita/03_Frontend/cotizacion-app/src/app/core/guards/auth.guard.ts)
- **Vistas y Estilos del Login:** [login.component.ts](file:///d:/Proyectos/Pumita/03_Frontend/cotizacion-app/src/app/features/login/login.component.ts)
- **Enrutador:** Configurado en [app.routes.ts](file:///d:/Proyectos/Pumita/03_Frontend/cotizacion-app/src/app/app.routes.ts) para proteger los paths.
- **Layout Condicional:** Modificado en [app.html](file:///d:/Proyectos/Pumita/03_Frontend/cotizacion-app/src/app/app.html) y [navbar.component.html](file:///d:/Proyectos/Pumita/03_Frontend/cotizacion-app/src/app/shared/components/navbar/navbar.component.html) para mostrar perfiles y desloguearse.

---

## Servidor de Desarrollo

Para iniciar el servidor local de desarrollo de Angular, ejecuta:

```bash
npm install
npm start
```

Una vez que compile e inicie, abre tu navegador en `http://localhost:4200/`. El sistema te redirigirá automáticamente a `/login` si no has iniciado sesión.

---

## Pruebas y Compilación

### Compilar Proyecto
Para generar la build de producción optimizada en la carpeta `dist/`:
```bash
npm run build
```

### Ejecutar Pruebas Unitarias
Para correr las pruebas unitarias usando el test runner **Vitest**:
```bash
npm test
```
