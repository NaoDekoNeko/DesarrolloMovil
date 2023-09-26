package empresa.android.dao;

import android.widget.Toast;

import empresa.android.bean.ProductoBean;

public class ProductoDAO {
    ProductoBean objProductoBean;

    public ProductoDAO(ProductoBean objProductoBean){
        this.objProductoBean = objProductoBean;
    }

    public String calcularOperacion(){
        String mensaje = "";

        int costo = calcularCostoParZapatillas(this.objProductoBean);
        this.objProductoBean.setCosto(costo);

        int venta = calcularVenta(this.objProductoBean);
        this.objProductoBean.setVenta(venta);

        double descuento = calcularDescuento(this.objProductoBean);
        this.objProductoBean.setDescuento(descuento);

        double ventaNeta = calcularVentaNeta(this.objProductoBean);
        this.objProductoBean.setVentaNeta(ventaNeta);

        mensaje = "El costo del par de zapatillas: " + costo +"\n"+
                "La venta de zapatillas es: " + venta + "\n"+
                "El descuento es: " + descuento + "\n"+
                "La venta neta es:" + ventaNeta + "\n";

        return mensaje;
    }

    public int calcularVenta(ProductoBean objProductoBean){
        return objProductoBean.getCosto()* objProductoBean.getNumPares();
    }

    public double calcularDescuento(ProductoBean objProductoBean){
        if (objProductoBean.getNumPares()>=2 && objProductoBean.getNumPares()<=5)
            return 0.05*objProductoBean.getVenta();
        if(objProductoBean.getNumPares()>=6 && objProductoBean.getNumPares()<=10)
            return 0.08*objProductoBean.getVenta();
        if(objProductoBean.getNumPares()>=10 && objProductoBean.getNumPares()<=20)
            return 0.1*objProductoBean.getVenta();
        if(objProductoBean.getNumPares()>20)
            return 0.15*objProductoBean.getVenta();
        return 0;
    }

    public double calcularVentaNeta(ProductoBean objProductoBean){
        return objProductoBean.getVenta()-objProductoBean.getDescuento();
    }

    public int calcularCostoParZapatillas(ProductoBean objProductoBean){
        int costo = 0;

        switch (objProductoBean.getMarca()){
            case 0:{
                switch (objProductoBean.getTalla()){
                    case 0: costo = 140; break;
                    case 1: costo = 160; break;
                    case 2: costo = 160; break;
                }
                break;
            }
            case 1:{
                switch (objProductoBean.getTalla()){
                    case 0: costo = 140; break;
                    case 1: costo = 150; break;
                    case 2: costo = 150; break;
                }
                break;
            }
            case 2:{
                switch (objProductoBean.getTalla()){
                    case 0: costo = 80; break;
                    case 1: costo = 85; break;
                    case 2: costo = 90; break;
                }
                break;
            }
        }
        return costo;
    }
}
