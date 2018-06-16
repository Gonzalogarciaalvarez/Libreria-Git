/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libreriagit_cod;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.swing.JOptionPane;
import org.eclipse.*;

/**
 *Esta clase tiene los metodos a los que se llaman desde la clase menu
 * @author Gonzalo
 */
public class Metodos {
    /**
     * Este metodo estatico nos permite crear un repositorio de GitHub desde Netbeans,
     * añadiendo tus credenciales en un archivo del pc
     * @throws IOException  Indica que se ha producido un error en la entrada/salida de informacion
     */
     public static void crearRepositorio() throws IOException {
        
        String nombre = JOptionPane.showInputDialog("¿Que nombre le quieres dar al repositorio?");

        GitHub github = GitHub.connect();
        GHCreateRepositoryBuilder builder;
        builder = github.createRepository(nombre);
        builder.create();
    }
     
     /**
      * Este método estatico nos permite clonar un proyecto desde un repositorio 
      * de GitHub (del cual le pasamos la URL), a una carpeta de nuestro pc(de la
      * cual le pasamos la ruta) mediante un JOptionPane
      * @throws GitAPIException salta la excepcion si se produjo un problema con la Api
      */
    public static void clonar() throws GitAPIException {
     
     String repo = JOptionPane.showInputDialog("Indica el repositorio del proyecto");
     String ruta = JOptionPane.showInputDialog("Indica la ruta de la carpeta"); 
     
        Git.cloneRepository()
                .setURI(repo)
                .setDirectory(new File(ruta))
                .call();
    }
    
    /**
     * Este metodo estatico inicializa el repositorio de un proyecto, del cual le
     * pasamos la ruta
     * @throws GitAPIException salta la excepcion si se produjo un problema con la Api
     */
    public static void inicializarRepositorio() throws GitAPIException {
        
        String ruta = JOptionPane.showInputDialog("Indica la ruta del proyecto");

        InitCommand init = new InitCommand();
        init.setDirectory(new File(ruta))
                .call();

    }
    /**
     * Metodo estatico que nos permite crear un commit siempre y cuando el proyecto
     * ya este inicializado, debemos indicar la ruta del proyecto y el nombre que 
     * deseamos ponerle al commit 
     * @throws IOException Significa que se ha producido un error en la entrada/salida
     * @throws GitAPIException salta la excepcion si se produjo un problema con la Api
     */
    public static void crearCommit() throws IOException, GitAPIException {
        
     String ruta = JOptionPane.showInputDialog("Indica la ruta del proyecto");
     String nombre = JOptionPane.showInputDialog("¿Como deseas nombrar el commit?");
       
        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
        Repository repository = repositoryBuilder.setGitDir(new File(ruta+".git"))
                .readEnvironment()
                .findGitDir()
                .setMustExist(true)
                .build();
        Git git = new Git(repository);      
        AddCommand add = git.add();
        add.addFilepattern(ruta+".git").call();
        CommitCommand commit = git.commit();
        commit.setMessage(nombre).call();
    }
    /**
     * Metodo estatico que nos permite subir el proyecto a GitHub siempre y cuando
     * este inicializado y el contenido aparezca guardado en un commit, para que
     * este metodo funcione debemos pasarle la ruta del proyecto, la URI del repositorio
     * de GitHub, y las credenciales de nuestra cuenta.
     * @throws IOException Significa que se ha producido un error en la entrada/salida
     * @throws GitAPIException salta la excepcion si se produjo un problema con la Api
     * @throws URISyntaxException salta la excepcion si se produjo un problema con la url
     */
     public static void hacerPush() throws IOException, GitAPIException, URISyntaxException{
        String ruta = JOptionPane.showInputDialog("Indica la ruta del proyecto");
        String repo = JOptionPane.showInputDialog("Indica la URI del proyecto");
        String usuario = JOptionPane.showInputDialog("Introduce usuario de GitHub");
        String contraseña = JOptionPane.showInputDialog("Introduce contraseña de GitHub");
        
         FileRepositoryBuilder constructorRepositorio = new FileRepositoryBuilder();
        Repository repositorio = constructorRepositorio.setGitDir(new File(ruta+".git"))
                .readEnvironment() 
                .findGitDir() 
                .setMustExist(true)
                .build();
        
        Git git = new Git(repositorio);

        RemoteAddCommand remoteAddCommand = git.remoteAdd();
        remoteAddCommand.setName("origin");
        remoteAddCommand.setUri(new URIish(repo));
        remoteAddCommand.call();

        PushCommand pushCommand = git.push();
        pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(usuario, contraseña));
        pushCommand.call();
        
    }
}
