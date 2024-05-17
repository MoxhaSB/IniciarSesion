import Interface.Lista;

import java.util.ArrayList;

public class ListaUsuario implements Lista<Usuario> {

    private ArrayList<Usuario> lista;

    public ListaUsuario(){
        this.lista = new ArrayList<>();
    }
    public ArrayList<Usuario> getLista(){
        return this.lista;
    }
}
