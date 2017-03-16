package io.ckl.boilerplate.api_services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import io.ckl.boilerplate.models.Greeting;

/**
 * This is just a mock, a API Service class would actually get the data from a API using Retrofit
 */
public class GreetingAPIService {

    private static List<String> greetingsList = new ArrayList<>(Arrays.asList("Hallo, wêreld!","Pershëndetje Botë","أهلاً بالعالم","Բարե՛ւ, աշխարհ։","Salam Dünya","Ahoj Světe!","Kaixo mundua!","Прывітанне свет","Shani Mwechalo!","Shagatam Prithivi!","Zdravo Svijete!","Здравей, свят!","ជំរាបសួរ ពិភពលោក","Hola món!","你好世界","ᎣᏏᏲ ᎡᎶᎯ","Klahowya Hayas Klaska","Bok Svijete!","Hej, Verden!","Hallo, wereld!","Hello World!","Saluton mondo!","Tere maailm!","Hei maailma!","Salut le Monde!","Hallo, wrâld!","Ola mundo!","Hallo Welt!","Γεια σου κόσμε!","Aloha Honua","שלום עולם","नमस्ते दुनिया","Nyob zoo ntiaj teb.","Helló világ!","Halló heimur!","Ndewo Ụwa","Halo Dunia!","Dia dhaoibh, a dhomhain!","Ciao Mondo!","こんにちは、 世界！","ಹಲೋ ವರ್ಲ್ಡ್","Habari dunia!","Niatia thi!","nuqneH","반갑다 세상아","ໂລກ","AVE MVNDE","Sveika, Pasaule!","Sveikas, Pasauli","Moien Welt!","Manao ahoana ry tany!","Namaskaram, lokame","Merhba lid-dinja","Hallo verden!","سلام دنیا","Witaj świecie!","Olá, mundo!","ਸਤਿ ਸ੍ਰੀ ਅਕਾਲ ਦੁਨਿਆ","Salut lume!","Здравствуй, мир!","Zdravo Svete!","Ahoj, svet!","Pozdravljen svet!","¡Hola mundo!","Hejsan världen!","Kamusta mundo!","ஹலோ உலகம்","హలో వరల్డ్","สวัสดีโลก!","Merhaba Dünya!","Привiт, свiте!","ہیلو دنیا والو","Xin chào thế giới","S'mae byd!","העלא וועלט","Sawubona Mhlaba"));

    public static Greeting fetchGreeting() {
        String greetingText = greetingsList.get(new Random().nextInt(greetingsList.size()));
        return new Greeting(greetingText);
    }
}
