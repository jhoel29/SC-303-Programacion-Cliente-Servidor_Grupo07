/**
* Clase Hija - hereda los metodos de registroCompras y listarCompras de la interface Factura
* Clase para la administracion de los productos que se venderan en el taller.
* 
* Esta clase incluye: 
* - Registro de Productos 
* - Registro de la compra realizada
* - Listado de la compra realizada
 */
package Gestores;
import Producto.Producto;
import Persona.Cliente;
import Factura.Factura;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GestorAdministracionProductos implements Factura {
    
    //Atributo unico de la clase GestorAdministracionProductos
    private float totalPagado;
  
    // Metodo para ingresar un producto a la base de datos
    public void ingresarProducto(Producto producto) {
        try {
            // Establecer conexion a la base de datos
            Connection conexion = conectar();
            
            // Consulta SQL para insertar un producto en la tabla "productos"
            String consulta = "INSERT INTO productos (codigoProducto, nombre, descripcion, precio, cant_stock) VALUES (?, ?, ?, ?, ?)";
            
            // Preparar la declaración SQL
            PreparedStatement declaracion = conexion.prepareStatement(consulta);
            declaracion.setInt(1, producto.getCodigoProducto());
            declaracion.setString(2, producto.getNombre());
            declaracion.setString(3, producto.getDescripcion());
            declaracion.setDouble(4, producto.getPrecio());
            declaracion.setInt(5, producto.getCantidadStock());
            
            // Ejecutar la consulta
            declaracion.executeUpdate();
            
            // Cerrar la conexion
            conexion.close();
            
            System.out.println("Producto ingresado correctamente en la base de datos.");
        } catch (SQLException e) {
            System.out.println("Error al ingresar el producto en la base de datos: " + e.getMessage());
        }
    }
    
    public void listarProductos(){
        try {
            // Establecer conexion a la base de datos
            Connection conexion = conectar();
            
            // Consulta SQL para seleccionar todos los registros de los productos
            String consulta = "SELECT * FROM productos";
            
            // Preparar la declaración SQL
            PreparedStatement declaracion = conexion.prepareStatement(consulta);
            
            // Ejecutar la consulta y obtener el resultado
            ResultSet resultado = declaracion.executeQuery();
            
            // Imprimir los resultados
            System.out.println("Listado de productos:");
            while (resultado.next()) {
                int cod_producto = resultado.getInt("codigoProducto");
                String nombre = resultado.getString("nombre");
                String descripcion = resultado.getString("descripcion");
                float precio = resultado.getFloat("precio");
                int cantidad_stock = resultado.getInt("cant_stock");
                
                System.out.println("Codigo Producto: " + cod_producto + ", Nombre: " + nombre + ", Descripcion: " + descripcion + ", Precio: " + precio + ", Cantidad en Stock: " + cantidad_stock);
            }
            
            // Cerrar la conexión
            conexion.close();
        } catch (SQLException e) {
            System.out.println("Error al listar los productos de la base de datos: " + e.getMessage());
        }
    }
    
    //Metodo sobreescrito de la interface factura - Registra la compra en la base de datos "Taller" en la tabla "RegistroCompras"
    @Override
    public void registroVentas(Cliente cliente, float totalPagado, TipoPago tipoPago) {
        try {
            // Establecer conexión a la base de datos
            Connection conexion = conectar();
            
            // Consulta SQL para insertar la transaccion en la tabla de "registroventasproductos"
            String consulta = "INSERT INTO ventasproductos (Cliente, TotalPagado, TipoPago) VALUES (?, ?, ?)";
            
            // Se realiza el armado de la consulta
            PreparedStatement declaracion = conexion.prepareStatement(consulta);
            declaracion.setString(1, cliente.getNombre()); 
            declaracion.setFloat(2, totalPagado);
            declaracion.setString(3, tipoPago.toString());
            
            // Ejecuta la insercion
            declaracion.executeUpdate();
            
            // Cierra la conexion
            conexion.close();
            
            System.out.println("Transacción de venta guardada en la base de datos.");
        } catch (SQLException e) {
            System.out.println("Error al guardar la transacción de venta en la base de datos: " + e.getMessage());
        }
    }


    //Metodo sobreescrito de la interface factura - Lista las compras realizadas que se guardaron en la tabla "registrocompras"
    @Override
    public void listarVentas() {
        try {
            // Establecer conexion a la base de datos
            Connection conexion = conectar();
            
            // Consulta SQL para seleccionar todos los registros de ventas de productos
            String consulta = "SELECT * FROM ventasproductos";
            
            // Preparar la declaracion SQL
            PreparedStatement declaracion = conexion.prepareStatement(consulta);
            
            // Ejecutar la consulta y obtener el resultado
            ResultSet resultado = declaracion.executeQuery();
            
            // Imprimir los resultados
            System.out.println("Listado de ventas:");
            while (resultado.next()) {
                int id = resultado.getInt("id");
                String cliente = resultado.getString("Cliente");
                float total_pagado = resultado.getFloat("TotalPagado");
                String tipo_pago = resultado.getString("TipoPago");
                
                System.out.println("ID Venta: " + id + ", Cliente: " + cliente + ", Total Pagado: " + total_pagado + ", Tipo de Pago: " + tipo_pago);
            }
            
            // Cerrar la conexion
            conexion.close();
        } catch (SQLException e) {
            System.out.println("Error al listar las ventas de la base de datos: " + e.getMessage());
        }
    }
    
     //Metodo para conectarse a la base de datos
     public static Connection conectar() throws SQLException {
        // Datos de conexión a la base de datos
        String url = "jdbc:mysql://localhost:3306/taller";
        String usuario = "root";
        String contrasena = "root";

        // Establecer la conexion y retornarla
        return DriverManager.getConnection(url, usuario, contrasena);
    }   
 
}
