package beans;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class CodenationJulio {
    
    private static HttpURLConnection connection;
    public static void main(String[] main){ 
    try{
        // Main class, onde manipulamos oque foi feito nas classes 
        FileWriter file = null;  
        JSONObject obj;
        JSONParser parser = new JSONParser();
        // Cria um arquivo json com a api do codenation
        file = new FileWriter("src/codenationjulio/answer.json");
        file.write(httpreq());
        file.close();
        
        //pega os valores da chave do arquivo json
        obj = (JSONObject) parser.parse(new FileReader("src/codenationjulio/answer.json"));
        String cifrado = (String) obj.get("cifrado");
        String token = (String) obj.get("token");
        int id = Integer.parseInt(obj.get("numero_casas").toString());
        
        //Descriptografa o elemento cifrado de julio cesar
        String juliocesar = criptoJulio(cifrado.toString(), id);         
        //Criptografa em hash md5 a String descriptografada.
        String md5 = criptoMD5(juliocesar);
        
        //Insere os elementos modificados no elementos JSON
        JSONObject modobj=new JSONObject();
        modobj.put("numero_casas", id);
        modobj.put("token", token);
        modobj.put("cifrado", cifrado);
        modobj.put("decifrado", juliocesar);
        modobj.put("resumo_criptografico", md5);
        file = new FileWriter("src/codenationjulio/answer.json");
        file.write(modobj.toJSONString());
        String fon = modobj.toJSONString();
        file.close();
        
    }
    catch(IOException e){}
    catch(ParseException ex){}
    finally{
        connection.disconnect();
    }
}
    
    //Método que faz o request da api do desafio.
    public static String httpreq(){
    //requisição http
    BufferedReader reader;
    String line;
    StringBuffer responseContent = new StringBuffer();
    try{
        URL url = new URL("https://api.codenation.dev/v1/challenge/dev-ps/generate-data?token=133bbb58246deaa1d8eb7f2e789ea174db19917e");
        connection = (HttpURLConnection) url.openConnection();
        
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        
        // Status da conexão
        int status = connection.getResponseCode();
        
        // Para erros na conexão da api
        if (status>299){
            reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            while((line = reader.readLine())!=null){
                responseContent.append(line);
            }
            reader.close();
        }else{
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while((line = reader.readLine())!=null){
                responseContent.append(line);
            }
            reader.close();
            
        }
    }
    catch(MalformedURLException e){e.printStackTrace();}
    catch(IOException e){e.printStackTrace();}
    finally{
        connection.disconnect();
    }return responseContent.toString();
    }
    
    
    
    //Classe que descriptografia o algoritmo de Julio Cesar.
    public static String criptoJulio(String texto, int numcasa){
        // Inicio uma array com todos as letras do alfabeto
        String[] abcdario = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        // ArrayList para receber o descriptografado
        List<String> decifText = new ArrayList<String>();
        // Inicio um for para ler letra por letra do texto criptografado
        for(int i = 0;i<texto.length(); i++){
            //Pego o char do texto criptografado e o transformo em string
            char letra = texto.charAt(i);
            String asString = String.valueOf(letra);
            // Transformo o abcdario em arraylist
            List<String> abc = Arrays.asList(abcdario);
            // Estrutura de condição para verificar se tem a letra na arraylist do abc
            if(abc.contains(asString.toLowerCase())){
                //Estrutura de repetição para pegar qual a posição da letra do texto no alfabeto para subtrair
                for(int j = 0; j < 26; j++){ 
                    //Se a letra do texto estiver no abcdario então ele continua
                    if(asString.equalsIgnoreCase(abcdario[j])){
                        // Pega a posição decifrada do elemento do texto (LetraAlf - NumCasa) 
                        int pos = j-numcasa;
                        if(pos<0){
                            // Calcula a posição e adiciona no arrayList "decifText"
                            pos = 25 + (pos + 1);
                            decifText.add(abcdario[pos]);
                        }else{
                            // Calcula a posição e adiciona no arrayList "decifText"
                            decifText.add(abcdario[pos]);
                        }                   
                    }
                }
            // Se não conter o texto na arraylist então adiciona a String na arraylist "decifText" sem nenhuma modificação
            }else{
                decifText.add(asString);
            }
        }
        // Converte a arraylist em uma String
        String decod = "";
        StringBuilder str = new StringBuilder(decod);
        for(String s:decifText){
            str.append(s);
        } 
        String strs = str.toString();
        return strs;
    }
    
    
    
    //Criptografia MD5
    public static String criptoMD5(String senha){
        String retorno = "";
        MessageDigest md;
        try{
            md = MessageDigest.getInstance("MD5");
            BigInteger hash = new BigInteger(1, md.digest(senha.getBytes()));
            retorno = hash.toString(16);
        }catch(Exception e){}
        return retorno;  
    }
}
