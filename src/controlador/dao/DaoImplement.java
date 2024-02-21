/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador.dao;

import com.thoughtworks.xstream.XStream;
import controlador.TDA.listas.DynamicList;
import java.io.FileReader;
import java.io.FileWriter;

/**
 *
 * @author sebastian
 */
public class DaoImplement<T> implements DaoInterface<T>{
    private Class<T> clazz;
    private XStream conection;
    private String URL;

    public DaoImplement(Class<T> clazz) {
        this.clazz = clazz;
        conection = Bridge.getConection();
        URL = Bridge.URL+clazz.getSimpleName()+".json";
    }

    @Override
    public Boolean persist(T data) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        DynamicList<T> ld = all();
        ld.add(data);
        try {
           conection.toXML(ld, new FileWriter(URL));
           return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean merge(T data, Integer index) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        DynamicList<T> ld = all();
        
        try {
            ld.merge(data, index);
           conection.toXML(ld, new FileWriter(URL));
           return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public DynamicList<T> all() {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        DynamicList<T> dl = new DynamicList<>();
        try {
            dl = (DynamicList<T>)conection.fromXML(new FileReader(URL));
        } catch (Exception e) {
        }
        return dl;
    }

    @Override
    public T get(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public XStream getConection() {
        return conection;
    }

       
}









