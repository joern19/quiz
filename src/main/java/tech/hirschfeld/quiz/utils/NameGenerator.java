package tech.hirschfeld.quiz.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NameGenerator {

  private static final String[] ALL_NAMES = {
    "Anakin Skywalker",
    "BB-8",
    "C-3PO",
    "Chewbacca",
    "Finn",
    "FN-2187",
    "Han Solo",
    "Lando Calrissian",
    "Leia Organa",
    "Luke Skywalker",
    "Mace Windu",
    "Obi-Wan/Ben Kenobi",
    "Padmé Amidala",
    "Poe Dameron",
    "Qui-Gon Jinn",
    "R2-D2",
    "Rey Skywalker",
    "Yoda",
    "Jyn Erso",
    "Cassian Andor",
    "K-2SO",
    "Chirrut Îmwe",
    "Baze Malbus",
    "Tobias Beckett",
    "Antagonisten",
    "Darth Vader",
    "Armitage Hux",
    "Darth Maul",
    "Ben Solo", 
    "Kylo Ren",
    "Boba Fett",
    "Count Dooku/Darth Tyranus",
    "Dryden Vos",
    "Jabba der Hutte",
    "Moff Gideon",
    "Sheev Palpatine/Darth Sidious",
    "Snoke",
    "Orson Krennic",
    "Phasma",
    "Qi’ra",
    "Wilhuff Tarkin",
    "Nebenfiguren",
    "Aayla Secura",
    "Adi Gallia",
    "Admiral Gial Ackbar",
    "Admiral Firmus Piett",
    "Agent Alexsandr Kallus/Fulcrum II.",
    "Ahsoka Tano/Fulcrum I.",
    "Amilyn Holdo",
    "Aurra Sing",
    "Bail Organa",
    "Barriss Offee",
    "Beru Lars",
    "Bodhi Rook",
    "Boss Nass",
    "Bossk",
    "C1-10P/Chopper",
    "C'ai Threnalli",
    "Captain Raymus Antilles",
    "Captain Rex",
    "Captain Quarsh Panaka",
    "Captain Gregar Typho",
    "Commander Cody",
    "Darth Bane",
    "Din Djarin/Der Mandalorianer",
    "DJ",
    "Enfys Nest",
    "Enric Pryde",
    "Ezra Bridger",
    "Galen Erso",
    "Garazeb Orrelios",
    "Grievous",
    "Großadmiral Thrawn",
    "Großinquisitor",
    "Hera Syndulla",
    "Hondo Ohnaka",
    "Jar Jar Binks",
    "Jan Dodonna",
    "Jannah/TZ-1719",
    "Jango Fett",
    "Kanan Jarrus/Caleb Dume",
    "Kanzler Finis Valorum",
    "Kazuda Xiono",
    "Kaydel Ko Connix",
    "Ki-Adi-Mundi",
    "Kit Fisto",
    "Lady Proxima",
    "Larma D'Acy",
    "Lor San Tekka",
    "Luminara Unduli",
    "Maz Kanata",
    "Mon Mothma",
    "Nien Nunb",
    "Nute Gunray",
    "Owen Lars",
    "Plo Koon",
    "Rio Durant",
    "Rose Tico",
    "Sabine Wren",
    "Saw Gerrera",
    "Shaak Ti",
    "Shmi Skywalker",
    "Val",
    "Watto",
    "Wedge Antilles",
    "Wicket W. Warrick",
    "Zorii Bliss",
    "Bria Tharen",
    "Cade Skywalker",
    "Galen Marek/Starkiller",
    "Jacen Solo/Darth Caedus",
    "Jaina Solo",
    "Kyle Katarn",
    "Mara Jade Skywalker",
    "Darth Malak",
    "Darth Revan"
  };

  private final Queue<String> avaibleNames;
  private Integer counterIfThereAreNoMoreNames = 0; // starts with 1 (see getUniqueName)

  public NameGenerator() {
    LinkedList<String> avaibleNames = new LinkedList<String>(Arrays.asList(ALL_NAMES));
    Collections.shuffle(avaibleNames);
    this.avaibleNames = avaibleNames;
  }


  /**
   * Get a Name wich is Unique in this Instance. 
   * If there are no more Names left(there are more then 100),
   * a counter is returned string at 1 
   * @return an unique Name
   */
  public String getUniqueName() {
    if (avaibleNames.isEmpty()) {
      counterIfThereAreNoMoreNames++;
      return counterIfThereAreNoMoreNames.toString();
    }
    return avaibleNames.poll();
  }

}
