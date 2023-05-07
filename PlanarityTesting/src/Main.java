import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
public class Main {
    public static String readFile(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line = br.readLine(); String result = "";
        while(line != null) {result += line + "\n"; line = br.readLine(); }
        return result;
    }
    public static void main(String[] args) {
        Random rand = new Random();

//______________________________________________________ENTRY___________________________________________________________
        String input_0 = "";

        try{
            //input_0 = readFile("matrix.txt");
            input_0 = readFile("matrix1.txt");
        }
        catch (IOException e) {System.err.println("ERROR! WRONG INPUT!"); e.printStackTrace();}

        System.out.println("\nINPUT: \n" + input_0);

        String[] sorok = input_0.split("\n");

        int V = sorok.length;

        int[][] matrix = new int[V][V];
        int[][] input = new int[V][V];
        for(int i = 0; i < V; i++) {
            String[] csucsok = sorok[i].split(" ");
            for(int j = 0; j < V; j++) {
                matrix[i][j] = Integer.parseInt(csucsok[j]);
                input[i][j] = Integer.parseInt(csucsok[j]);
            }
        }
        Graph G = new Graph(matrix);
//______________________________________________________ENTRY___________________________________________________________


//______________________________________________EDGE_NUMBER_CONDITION___________________________________________________
        int E = 0;
        for(int i = 0; i < V; i++)for(int j = 0; j< V; j++) E = E + matrix[i][j];
        E = E/2;

        System.out.print("V = " + V); System.out.println(", E = " + E);
        System.out.println("_____________________________________________");

        if(E > 3*V-3) { System.out.println("Nem síkgráf, mert E > 3*V-3"); System.out.println(); return; }
//______________________________________________EDGE_NUMBER_CONDITION___________________________________________________

        int[][] rajz = new int[V][V];
        int[][] elek = new int[V][V];

//______________________________________________FINDING_CYCLE_IN_G1_____________________________________________________
        System.out.println(); System.out.println("G1:\n");

        for(int i= 0; i < sorok.length; i++) G.getValue()[i] = 0;
        int v = rand.nextInt(sorok.length); G.setK(0); int[] c; G.getCycle()[0] = v+1;

        c = G.find_cycle(v, 0);

        int[] igazi = new int[sorok.length+1]; int vege = 0; int j = 0; int meret = 0;

        for(int i = sorok.length; i >= 0 ; i--){
            if(c[i]!=0 && vege == 0) {
                vege = c[i];
                igazi[0] = vege; meret++;
                continue;
            }
            if(vege != 0) {
                j++;
                igazi[j] = c[i]; meret++;
            }
            if(vege != 0 && c[i] == vege) break;
        }

        for(int i = 0;i < igazi.length; i++) {
            if(i!=0 && igazi[i]!=0) {
                elek[igazi[i-1]-1][igazi[i]-1] = 1;
                elek[igazi[i]-1][igazi[i-1]-1] = 1;
            }
        }

        int[] legigazibb = new int[meret-1];
        for(int i = 0; i < meret-1; i++) {
            legigazibb[i] = igazi[i];
        }

        int[] G1 = legigazibb;
//______________________________________________FINDING_CYCLE_IN_G1_____________________________________________________


//_________________________________________FINDING_DEPRAVED_BRIDGES_IN_G1_______________________________________________
        int[] nemlehet = new int[2];
        for(int k=0; k<legigazibb.length; k++) {
            if(k != 0 && k != legigazibb.length-1) {
                nemlehet[0] = legigazibb[k - 1];
                nemlehet[1] = legigazibb[k + 1];
            } else if (k == 0) {
                nemlehet[0] = legigazibb[legigazibb.length-1];
                nemlehet[1] = legigazibb[k + 1];
            } else {
                nemlehet[0] = legigazibb[k - 1];
                nemlehet[1] = legigazibb[0];
            }
        }

        int[][] elfajultak = new int[E][2];
        int elfajult_meret = 0;
        int[] elfajultak_tartomanya = new int[E];

        for(int l=0; l< legigazibb.length; l++) {
            for(int q=0; q< legigazibb.length; q++) {
                if(input[legigazibb[l]-1][legigazibb[q]-1] == 1 &&
                        rajz[legigazibb[l]-1][legigazibb[q]-1] == 0
                        && elek[legigazibb[l]-1][legigazibb[q]-1] == 0) {
                    if(l ==0) {
                        if(legigazibb[l+1] != legigazibb[q] && legigazibb[legigazibb.length-1] != legigazibb[q]) {
                            elfajultak[elfajult_meret][0] = legigazibb[l]; elfajultak[elfajult_meret][1] = legigazibb[q];
                            elfajultak_tartomanya[elfajult_meret] = 1;
                            elfajult_meret++;
                        }
                    }
                    else if(l == legigazibb.length-1) {
                        if(legigazibb[0] != legigazibb[q] && legigazibb[l-1] != legigazibb[q]) {
                            elfajultak[elfajult_meret][0] = legigazibb[l]; elfajultak[elfajult_meret][1] = legigazibb[q];
                            elfajultak_tartomanya[elfajult_meret] = 1;
                            elfajult_meret++;
                        }
                    } else {
                        if(legigazibb[l+1] != legigazibb[q] && legigazibb[l-1] != legigazibb[q]) {
                            elfajultak[elfajult_meret][0] = legigazibb[l]; elfajultak[elfajult_meret][1] = legigazibb[q];
                            elfajultak_tartomanya[elfajult_meret] = 1;
                            elfajult_meret++;
                        }
                    }
                }
            }
        }

        if(elfajult_meret == 0) System.out.println("-nincsenek elfajult hidak.");
        else {
            System.out.println("-elfajultak:");
            for (int i = 0; i < elfajult_meret; i++) {
                System.out.println(elfajultak_tartomanya[i] + "-ben:");
                for (int l = 0; l < 2; l++) {
                    System.out.print(elfajultak[i][l] + " ");
                }
            }
        }
//_________________________________________FINDING_DEPRAVED_BRIDGES_IN_G1_______________________________________________


//__________________________________________________FACES_OF_G1_________________________________________________________
        int max_tart_szam = 2 + E - V; // tartományok szama G-ben ha az síkgráf
        int tart_szam = 0;
        int[][] tartomanyok = new int[max_tart_szam][V];
        int[] tel_tartomanyok = new int[max_tart_szam];
        for(int i=0; i< G1.length; i++) {
            tartomanyok[0][i] = G1[i]; tel_tartomanyok[0]++;
        } tart_szam++;
        for(int i=0; i< G1.length; i++) {
            tartomanyok[1][i] = G1[i]; tel_tartomanyok[1]++;
        } tart_szam++;

        System.out.println("-tartományok:");
        for(int i = 0; i < 2; i++) {
            System.out.print( "f"+ (i + 1) + ": ");
            for(int l = 0; l < tel_tartomanyok[i]; l++)System.out.print(tartomanyok[i][l] + " ");
            System.out.println();
        }
//__________________________________________________FACES_OF_G1_________________________________________________________


//_________________________________________CREATING_ADJACENCY_MATRIX_OF_G1______________________________________________
        int[][] incidenceG1 = new int[V][V];
        for(int i = 0; i < V; i++) {
            for(int l = 0; l < V; l++) {
                incidenceG1[i][l] = 0;
            }
        }
        for(int i= 0; i< G1.length; i++) {
            if(i != G1.length-1) {
                incidenceG1[G1[i]-1][G1[i + 1]-1] = 1;
                incidenceG1[G1[i+1]-1][G1[i]-1] = 1;
            } else {
                incidenceG1[G1[i]-1][G1[0]-1] = 1;
                incidenceG1[G1[0]-1][G1[i]-1] = 1;
            }
        }
        // System.out.println("G1 illeszkedési mátrixa");
        for(int i = 0; i < incidenceG1.length; i++) {
            for(int l = 0; l < incidenceG1.length; l++)  {
                // System.out.print(incidenceG1[l][i] + " ");
                if(incidenceG1[i][l] == 1)rajz[i][l] = 1;
            }
            // System.out.println();
        }
//_________________________________________CREATING_ADJACENCY_MATRIX_OF_G1______________________________________________


// lerajzoltuk az egészet?
        int rajzoltElek = G1.length; //G1 éleinek száma
        if(E - rajzoltElek == 0) {
            System.out.println("Síkgráf, mert sikerült lerajzolnunk az egész gráfot.");
            return;
        }
// lerajzoltuk az egészet?


//___________________________________FIND_NORMAL_BRIDGES(WITHOUT ATTACHMENTS)_IN_G1_____________________________________
        int[][] hidalapanyag = G.getIncidence();
        for(int i= 0; i< G1.length; i++) {
            for(int l = 0; l < hidalapanyag.length; l++) { hidalapanyag[G1[i]-1][l] = 0; hidalapanyag[l][G1[i]-1] = 0; }
        }
        int[][] G_G1_masolat = new int[V][V];
        for(int i = 0; i < V; i++) {
            String[] csucsok = sorok[i].split(" ");
            for(int l = 0; l < V; l++) { G_G1_masolat[i][l] = Integer.parseInt(csucsok[l]); }
        }
        int[] lehet_hidelem = new int[V];
        for(int i=0; i<V; i++) {
            lehet_hidelem[i] = 1;
        }
        for(int i=0; i<G1.length; i++) {
            if(G1[i] !=0) lehet_hidelem[G1[i]-1] = 0;
        }
        Graph G_G1 = new Graph(hidalapanyag);
        G_G1.find_components(lehet_hidelem);

        System.out.print("-hídpontjai(lábak nélkül):\nB1: ");
        for(int i= 0; i< G_G1.getHidakszama(); i++) {
            for(int l = 0; l < G_G1.getHidakhossza()[i]; l++) { System.out.print(G_G1.getHidak()[i][l] + " "); }
            if(i < G_G1.getHidakszama()-1)System.out.print("\nB"+(i+2)+": ");
            else System.out.println();
        }
//___________________________________FIND_NORMAL_BRIDGES(WITHOUT ATTACHMENTS)_IN_G1_____________________________________


//______________________________________FIND_ATTACHMENTS_OF_NORMAL_BRIDGES_IN_G1________________________________________
        int[] labak;
        int[] hidelem_incidence = new int[V];
        int voltlab; int labmeret; int nulla;
        for(int x=0; x < G_G1.getHidakszama(); x++) {
            labak = new int[V];
            labmeret = 0;
            for (int m = 0; m < G_G1.getHidak().length; m++) {
                nulla = 0;
                for (int i = 0; i < G.getIncidence().length; i++) {
                    if (G_G1.getHidak()[x][m] != 0) {
                        hidelem_incidence[i] = input[G_G1.getHidak()[x][m] - 1][i];
                    } else {
                        nulla++;
                        hidelem_incidence[i] = input[G_G1.getHidak()[x][m]][i];
                    }
                }
                if(nulla == hidelem_incidence.length) continue;
                for (int i = 0; i < V; i++) {
                    if (hidelem_incidence[i] == 1) {
                        for (int l = 0; l < G1.length; l++) {
                            if (G1[l] == i+1) {
                                voltlab = 0;
                                for (int n = 0; n < labmeret; n++) if (labak[n] == G1[l]) voltlab++;
                                if (voltlab == 0) {
                                    labak[labmeret] = G1[l];
                                    labmeret++;
                                }
                            }
                        }
                    }
                }
            }
            if(labmeret != 0) {
                for (int i = 0; i < labmeret; i++) {
                    G_G1.getHidlabak()[x][i] = labak[i];
                    if(labak[i] !=0)G_G1.getHidlabakhossza()[x]++;
                }
            }
        }
        System.out.print("-lábai:\nL1: ");
        for (int m = 0; m < G_G1.getHidakszama(); m++) {
            for (int i = 0; i < G_G1.getHidlabakhossza()[m]; i++)System.out.print(G_G1.getHidlabak()[m][i] + " ");
            if(m < G_G1.getHidakszama()-1)System.out.print("\nL"+(m+2)+": ");
            else System.out.println();
        }
//______________________________________FIND_ATTACHMENTS_OF_NORMAL_BRIDGES_IN_G1________________________________________


// F(B, G1) meghatározása minden B hídra
        int[][] hidaktartomanyai = new int [V][max_tart_szam];
        int[] hidaktartomanyainaksorszama = new int [V];
        int[] hidaktartomanyainakszama = new int[V];
        int hidelemszam=0;


        for(int i=0; i<tart_szam; i++) {
            for(int m=0; m < G_G1.getHidakszama(); m++) {
                hidelemszam =0;
                for(int n=0; n < G_G1.getHidlabakhossza()[m]; n++) {
                    for (int x = 0; x < tel_tartomanyok[i]; x++) {
                        if (G_G1.getHidlabak()[m][n] == tartomanyok[i][x]) hidelemszam++;
                    }
                }
                if (hidelemszam == G_G1.getHidlabakhossza()[m]) {
                    hidaktartomanyai[m][hidaktartomanyainaksorszama[m]] = i+1;
                    hidaktartomanyainaksorszama[m]++;
                }
            }
        }

        System.out.print("-F()-ek:\nF(B1,G1)={");
        for (int m = 0; m < G_G1.getHidakszama(); m++) {
            for (int i = 0; i < max_tart_szam; i++) {
                if(hidaktartomanyai[m][i]!=0 ) {
                    hidaktartomanyainakszama[m]++;
                    System.out.print("f"+hidaktartomanyai[m][i] + ", ");
                }
            }
            System.out.println("}");
            if(m < G_G1.getHidakszama()-1)System.out.print("F(B"+ (m+2)+",G1)={");
        }
        System.out.println();
        int kivalasztott = -1;
        int rajzolhatatlan = 0;

        for (int m = 0; m < G_G1.getHidakszama(); m++) {
            if(hidaktartomanyainakszama[m] == 0 && elfajult_meret == 0) rajzolhatatlan++;
            if(hidaktartomanyainakszama[m] == 1) {
                kivalasztott = m;

                System.out.println("F(B" +(0+1)+ ", G1)-ből:");
                System.out.print("f"+hidaktartomanyai[m][0] + ": ");
                for(int i=0; i<tel_tartomanyok[hidaktartomanyai[m][0]-1];i++) {
                    System.out.print(tartomanyok[hidaktartomanyai[m][0]-1][i] +" ");
                }
                for (int i = 0; i < max_tart_szam; i++) {
                    if(hidaktartomanyai[m][i]!=0 )hidaktartomanyainakszama[m]++;
                }
                break;
            }
            if(rajzolhatatlan == G_G1.getHidakszama()) {
                System.out.println("nem síkgráf");
                return;
            }
            if(kivalasztott == -1) {
                if(elfajult_meret > 0) {

                } else {
                    kivalasztott = 0;
                    System.out.println("F(B" +(0+1)+ ", G1)-ből:");
                    System.out.print("f"+hidaktartomanyai[0][0] + ": ");
                    for(int i=0; i<tel_tartomanyok[hidaktartomanyai[0][0]-1];i++) {
                        System.out.print(tartomanyok[hidaktartomanyai[0][0]-1][i] +" ");
                    }
                    for (int i = 0; i < max_tart_szam; i++) {
                        if (hidaktartomanyai[0][i] != 0) hidaktartomanyainakszama[0]++;
                    }
                }
            }
        }
// F(B, G1) meghatározása minden B hídra


// út megtalálása
        int [] A_hid = new int[V];
        int A_hid_hossza = 0;
        int [] A_hid_labak = new int[V];
        int A_hid_labak_hossza = 0;
        int[] voltmar = new int[V];

        if(kivalasztott != -1) {
            A_hid = G_G1.getHidak()[kivalasztott];
            A_hid_hossza = G_G1.getHidakhossza()[kivalasztott];
            A_hid_labak = G_G1.getHidlabak()[kivalasztott];
            A_hid_labak_hossza = G_G1.getHidlabakhossza()[kivalasztott];
        }

        for(int i=0; i<V; i++) {
            voltmar[i] = 0;
        }

        int kezd;
        int veg;
        int[] path;
        int tel_path;

        if(kivalasztott !=-1) {
            kezd = A_hid_labak[0];
            veg = 0;
            path = new int[V];
            tel_path = 0;
        } else {
            kezd = elfajultak[0][0];
            veg = elfajultak[0][1];
            path = new int[V];
            tel_path = 0;
        }


        path[0] = kezd; tel_path++;

        int jel;

        if(kivalasztott == -1) {
            path[1] = veg; tel_path++;
        } else {
            for (int i = 0; i < hidelem_incidence.length; i++) {
                hidelem_incidence[i] = 0;
            }
            jel = 0;
            nemlehet = new int[2];
            for (int k = 0; k < tel_tartomanyok[hidaktartomanyai[kivalasztott][0] - 1]; k++) {
                if (tartomanyok[hidaktartomanyai[kivalasztott][0] - 1][k] == kezd) {
                    if (k != 0 && k != tel_tartomanyok[hidaktartomanyai[kivalasztott][0] - 1] - 1) {
                        nemlehet[0] = tartomanyok[hidaktartomanyai[kivalasztott][0] - 1][k - 1];
                        nemlehet[1] = tartomanyok[hidaktartomanyai[kivalasztott][0] - 1][k + 1];
                    } else if (k == 0) {
                        nemlehet[0] = tartomanyok[hidaktartomanyai[kivalasztott][0] - 1][tel_tartomanyok[hidaktartomanyai[kivalasztott][0] - 1] - 1];
                        nemlehet[1] = tartomanyok[hidaktartomanyai[kivalasztott][0] - 1][k + 1];
                    } else {
                        nemlehet[0] = tartomanyok[hidaktartomanyai[kivalasztott][0] - 1][k - 1];
                        nemlehet[1] = tartomanyok[hidaktartomanyai[kivalasztott][0] - 1][0];
                    }
                }
            }

            jel = 0;

            for (int i = 0; i < V; i++) {
                if (input[path[0] - 1][i] == 1 && voltmar[i] == 0 && i != (kezd - 1)) {
                    if (i + 1 != nemlehet[0] && i + 1 != nemlehet[1]) {
                        path[1] = i + 1;
                        tel_path++;
                        voltmar[i] = 1;
                        for (int l = 0; l < A_hid_labak_hossza; l++) {
                            if (i == A_hid_labak[l] - 1) {
                                veg = path[1];
                                jel++;
                            }
                        }
                        break;
                    }
                }
            }


            for (int m = 1; m < V; m++) {
                for (int i = 0; i < V; i++) {
                    if (input[path[m] - 1][i] == 1 && voltmar[i] == 0 && i != (kezd - 1)) {
                        path[m + 1] = i + 1;
                        tel_path++;
                        voltmar[i] = 1;

                        for (int l = 0; l < A_hid_labak_hossza; l++) {
                            if (i == A_hid_labak[l] - 1) {
                                veg = path[m + 1];
                                jel++;
                            }
                        }
                        break;
                    }
                }
                if (jel != 0) break;
            }
        }

        System.out.println("\n\nAz út: ");
        for(int i=0; i< tel_path; i++) {
            if(i < tel_path-1) {
                elek[path[i]-1][path[i+1]-1] = 1;
                elek[path[i+1]-1][path[i]-1] = 1;
                rajz[path[i]-1][path[i+1]-1] = 1;
            }
            System.out.print(path[i] + " ");
        }
// út megtalálása


// G2 létrehozása
        int[] G2 = new int[V];
        int tel_G2=0;
        for(int i=0; i < G1.length; i++) {
            G2[i] = G1[i]; tel_G2++;
        }
        for(int i=1; i < tel_path-1;i++) {
            G2[tel_G2] = path[i]; tel_G2++;
        }
        System.out.println("\n___________________________");
        System.out.println("\nG2:\n");
        for(int i=0; i<tel_G2; i++) {
         //   System.out.print(G2[i] + " ");
        }
// G2 létrehozása


// tartományok frissítése

        int vegevan, index, ujtart_sorszam, tel_tartmasolat;
        int[] tartmasolat = new int[V];
        if (kivalasztott == -1) {
            for (int i = 0; i < tel_tartomanyok[elfajultak_tartomanya[0]-1]; i++) {
                tartmasolat[i] = tartomanyok[elfajultak_tartomanya[0] -1][i];
            }
            tel_tartmasolat = tel_tartomanyok[elfajultak_tartomanya[0] - 1];

            tel_tartomanyok[elfajultak_tartomanya[0] - 1] = 0;
            for (int i = 0; i < tartomanyok[elfajultak_tartomanya[0] - 1].length; i++) {
                tartomanyok[elfajultak_tartomanya[0] - 1][i] = 0;
            }
            for (int i = tel_path - 1, l = 0; i >= 0 && l < tel_path; i--, l++) {
                tartomanyok[elfajultak_tartomanya[0] - 1][l] = path[i];
                tel_tartomanyok[elfajultak_tartomanya[0] - 1]++;
            }
            vegevan = 0;
            index = tel_tartmasolat - 1;
            while (vegevan == 0) {
                if (tartmasolat[index] == kezd) {
                    int l = 0;
                    while (vegevan == 0) {
                        if (index > 0) {
                            index--;
                        } else {
                            index = tel_tartmasolat - 1;
                        }
                        if (tartmasolat[index] == veg) {
                            vegevan++;
                            break;
                        }
                        tartomanyok[elfajultak_tartomanya[0] - 1][tel_path + l] = tartmasolat[index];
                        tel_tartomanyok[elfajultak_tartomanya[0] - 1]++;
                        l++;
                    }
                }
                if (index > 0) {
                    index--;
                } else {
                    index = tel_tartmasolat - 1;
                }
            }


            vegevan = 0;
            index = 0;
            ujtart_sorszam = tart_szam;

            for (int i = tel_path - 1, l = 0; i >= 0 && l < tel_path; i--, l++) {
                tartomanyok[ujtart_sorszam][l] = path[i];
                tel_tartomanyok[ujtart_sorszam]++;
            }
            while (vegevan == 0) {
                if (tartmasolat[index] == kezd) {
                    int l = 0;
                    while (vegevan == 0) {
                        if (index < tel_tartmasolat - 1) {
                            index++;
                        } else {
                            index = 0;
                        }
                        if (tartmasolat[index] == veg) {
                            vegevan++;
                            break;
                        }
                        tartomanyok[ujtart_sorszam][tel_path + l] = tartmasolat[index];
                        tel_tartomanyok[ujtart_sorszam]++;
                        l++;
                    }
                }
                if (index < tel_tartmasolat - 1) {
                    index++;
                } else {
                    index = 0;
                }
            }
            tart_szam++;

        } else {
            for (int i = 0; i < tartmasolat.length; i++) {
                tartmasolat[i] = tartomanyok[hidaktartomanyai[kivalasztott][0] - 1][i];
            }
            tel_tartmasolat = tel_tartomanyok[hidaktartomanyai[kivalasztott][0] - 1];

            tel_tartomanyok[hidaktartomanyai[kivalasztott][0] - 1] = 0;
            for (int i = 0; i < tartomanyok[hidaktartomanyai[kivalasztott][0] - 1].length; i++) {
                tartomanyok[hidaktartomanyai[kivalasztott][0] - 1][i] = 0;
            }
            for (int i = tel_path - 1, l = 0; i >= 0 && l < tel_path; i--, l++) {
                tartomanyok[hidaktartomanyai[kivalasztott][0] - 1][l] = path[i];
                tel_tartomanyok[hidaktartomanyai[kivalasztott][0] - 1]++;
            }
            vegevan = 0;
            index = tel_tartmasolat - 1;
            while (vegevan == 0) {
                if (tartmasolat[index] == kezd) {
                    int l = 0;
                    while (vegevan == 0) {
                        if (index > 0) {
                            index--;
                        } else {
                            index = tel_tartmasolat - 1;
                        }
                        if (tartmasolat[index] == veg) {
                            vegevan++;
                            break;
                        }
                        tartomanyok[hidaktartomanyai[kivalasztott][0] - 1][tel_path + l] = tartmasolat[index];
                        tel_tartomanyok[hidaktartomanyai[kivalasztott][0] - 1]++;
                        l++;
                    }
                }
                if (index > 0) {
                    index--;
                } else {
                    index = tel_tartmasolat - 1;
                }
            }


            vegevan = 0;
            index = 0;
            ujtart_sorszam = tart_szam;

            for (int i = tel_path - 1, l = 0; i >= 0 && l < tel_path; i--, l++) {
                tartomanyok[ujtart_sorszam][l] = path[i];
                tel_tartomanyok[ujtart_sorszam]++;
            }
            while (vegevan == 0) {
                if (tartmasolat[index] == kezd) {
                    int l = 0;
                    while (vegevan == 0) {
                        if (index < tel_tartmasolat - 1) {
                            index++;
                        } else {
                            index = 0;
                        }
                        if (tartmasolat[index] == veg) {
                            vegevan++;
                            break;
                        }
                        tartomanyok[ujtart_sorszam][tel_path + l] = tartmasolat[index];
                        tel_tartomanyok[ujtart_sorszam]++;
                        l++;
                    }
                }
                if (index < tel_tartmasolat - 1) {
                    index++;
                } else {
                    index = 0;
                }
            }
            tart_szam++;
        }

        System.out.println("-tartományok:");
        for(int i = 0; i < tart_szam; i++) {
            System.out.print( "f"+ (i + 1) + ": ");
            for(int l = 0; l < tel_tartomanyok[i]; l++)System.out.print(tartomanyok[i][l] + " ");
            System.out.println();
        }
// tartományok frissítése


// G2 illeszkedési mátrixának létrehozása
        int[][] incidenceG2 = new int[V][V];
        for(int i = 0; i < V; i++) {
            for(int l = 0; l < V; l++) {
                incidenceG2[i][l] = input[i][l];
            }
        }
        int[] G1_masolat = new int[V];
        for(int i=0; i<G1.length; i++) {
            G1_masolat[i] = G1[i];
        }
        int[] reverseG2 = new int[V];
        for(int i=0; i<G2.length; i++) {
            if(G2[i]!=0)reverseG2[G2[i]-1] = 1;
        }
        for(int i= 0; i < V; i++) {
            if(reverseG2[i] == 0) {
                for(int l=0; l<V; l++) {
                    incidenceG2[l][i] = 0;
                    incidenceG2[i][l] = 0;
                }
            }
        }
       // System.out.println();
       // System.out.println("G2 illeszkedési mátrixa");
        for(int i = 0; i < incidenceG2.length; i++) {
            for(int l = 0; l < incidenceG2.length; l++) ;//System.out.print(incidenceG2[l][i] + " ");
       //     System.out.println();
        }
// G2 illeszkedési mátrixának létrehozása


// lerajzoltuk az egészet?
        rajzoltElek = rajzoltElek + tel_path-1; //G2 éleinek száma
        if(E - rajzoltElek == 0) {
            System.out.println("Síkgráf, mert sikerült lerajzolnunk az egész gráfot.");
            return;
        }
// lerajzoltuk az egészet?



        // kezdő értékek
        int[][] G_incidence_act = incidenceG2;
        int[] G_act = G2;
        Graph G_Gprev = G_G1;
        int[][] G_Gprev_masolat = G_G1_masolat;
        int tel_Gact = tel_G2;
        int[][] G_Gact_masolat = new int[V][V];
        int[] G_next = new int[V];
        int hanyadikG = 2;

// ciklus kezdete
        while(E - rajzoltElek != 0) {
    // Gact hídjainak megkeresése
            for(int i=1; i<tel_path-1; i++) {
                for(int l=0;l<A_hid_hossza; l++) {
                    if (path[i] == A_hid[l]) {
                        A_hid[l] = 0;
                    }
                }
            }
            for(int i= 0; i< G_Gprev.getHidakszama(); i++) {
                for(int l = 0; l < G_Gprev.getHidak().length; l++) {
                    //System.out.print(G_Gprev.getHidak()[i][l] + " ");
                }
                //System.out.println();
            }

            //System.out.println("\n");
            hidalapanyag = new int[V][V];
            for(int i = 0; i < V; i++) {
                for(int l = 0; l < V; l++) {
                    if(G_Gprev_masolat[i][l]==1)hidalapanyag[i][l] = 1;
                }
            }
            for(int i= 0; i< tel_Gact; i++) { // lenullázzuk G illeszkedési mátrixában Gprev csúcsainak megfelelő sorokat és oszlopokat
                for(int l = 0; l < hidalapanyag.length; l++) {
                    hidalapanyag[G_act[i]-1][l] = 0;
                    hidalapanyag[l][G_act[i]-1] = 0;
                }
            }

            for(int i=0; i<V; i++) {
                lehet_hidelem[i] = 1;
            }
            for(int i=0; i<V; i++) {
                if(G_act[i] !=0) lehet_hidelem[G_act[i]-1] = 0;
            }
            Graph G_Gact = new Graph(hidalapanyag);
            G_Gact.find_components(lehet_hidelem);

            System.out.print("-hídpontjai(lábak nélkül):\nB1: ");
            for(int i= 0; i< G_Gact.getHidakszama(); i++) {
                for(int l = 0; l < G_Gact.getHidakhossza()[i]; l++) {
                    System.out.print(G_Gact.getHidak()[i][l] + " ");
                }
                if(i < G_Gact.getHidakszama()-1)System.out.print("\nB"+(i+2)+": ");
                else System.out.println();
            }
    // Gact hídjainak megkeresése


    // Gact hidak lábainak meghatározása
            for(int x=0; x < G_Gact.getHidakszama(); x++) {
                labak = new int[V];
                labmeret = 0;
                for (int m = 0; m < G_Gact.getHidak().length; m++) {
                    nulla = 0;
                    for (int i = 0; i < G.getIncidence().length; i++) {
                        if (G_Gact.getHidak()[x][m] != 0) {
                            hidelem_incidence[i] = input[G_Gact.getHidak()[x][m] - 1][i];
                        } else {
                            nulla++;
                            hidelem_incidence[i] = input[G_Gact.getHidak()[x][m]][i];
                        }
                    }
                    if(nulla == hidelem_incidence.length) continue;
                    for (int i = 0; i < V; i++) {
                        if (hidelem_incidence[i] == 1) {
                            for (int l = 0; l < G_act.length; l++) {
                                if (G_act[l] == i+1) {
                                    voltlab = 0;
                                    for (int n = 0; n < labmeret; n++) if (labak[n] == G_act[l]) voltlab++;
                                    if (voltlab == 0) {
                                        labak[labmeret] = G_act[l];
                                        labmeret++;
                                    }
                                }
                            }
                        }
                    }
                }
                if(labmeret != 0) {
                    for (int i = 0; i < labmeret; i++) {
                        G_Gact.getHidlabak()[x][i] = labak[i];
                        if(labak[i] !=0)G_Gact.getHidlabakhossza()[x]++;
                    }
                }
            }
            System.out.print("-lábai:\nL1: ");
            for (int m = 0; m < G_Gact.getHidakszama(); m++) {
                for (int i = 0; i < G_Gact.getHidlabakhossza()[m]; i++)System.out.print(G_Gact.getHidlabak()[m][i] + " ");
                if(m < G_Gact.getHidakszama()-1)System.out.print("\nL"+(m+2)+": ");
                else System.out.println();
            }
    // Gact hidak lábainak meghatározása


    //elfajuló hidak keresése
            if(kivalasztott !=-1)
            for(int k=0; k<tel_tartomanyok[hidaktartomanyai[kivalasztott][0]-1]; k++) {
                if(tartomanyok[hidaktartomanyai[kivalasztott][0]-1][k] == kezd) {
                    if(k != 0 && k != tel_tartomanyok[hidaktartomanyai[kivalasztott][0]-1]-1) {
                        nemlehet[0] = tartomanyok[hidaktartomanyai[kivalasztott][0] - 1][k - 1];
                        nemlehet[1] = tartomanyok[hidaktartomanyai[kivalasztott][0] - 1][k + 1];
                    } else if (k == 0) {
                        nemlehet[0] = tartomanyok[hidaktartomanyai[kivalasztott][0] - 1][tel_tartomanyok[hidaktartomanyai[kivalasztott][0]-1]-1];
                        nemlehet[1] = tartomanyok[hidaktartomanyai[kivalasztott][0] - 1][k + 1];
                    } else {
                        nemlehet[0] = tartomanyok[hidaktartomanyai[kivalasztott][0] - 1][k - 1];
                        nemlehet[1] = tartomanyok[hidaktartomanyai[kivalasztott][0] - 1][0];
                    }
                }
            }

            elfajultak = new int[E][2];
            elfajult_meret = 0;

            for(int i=0; i < tart_szam; i++) {
                for(int l=0; l<tel_tartomanyok[i]; l++) {
                    for(int q=0; q<tel_tartomanyok[i]; q++) {
                        if(input[tartomanyok[i][l]-1][tartomanyok[i][q]-1] == 1 &&
                                rajz[tartomanyok[i][l]-1][tartomanyok[i][q]-1] == 0  &&
                                elek[tartomanyok[i][l]-1][tartomanyok[i][q]-1] == 0) {
                            if(l ==0) {
                                if(tartomanyok[i][l+1] != tartomanyok[i][q] && tartomanyok[i][tel_tartomanyok[i]-1] != tartomanyok[i][q]) {
                                    elfajultak[elfajult_meret][0] = tartomanyok[i][l]; elfajultak[elfajult_meret][1] = tartomanyok[i][q];
                                    elfajultak_tartomanya[elfajult_meret] = i+1;
                                    elfajult_meret++;
                                }
                            }
                            else if(l == tel_tartomanyok[i]-1) {
                                if(tartomanyok[i][0] != tartomanyok[i][q] && tartomanyok[i][l-1] != tartomanyok[i][q]) {
                                    elfajultak[elfajult_meret][0] = tartomanyok[i][l]; elfajultak[elfajult_meret][1] = tartomanyok[i][q];
                                    elfajultak_tartomanya[elfajult_meret] = i+1;
                                    elfajult_meret++;
                                }
                            } else {
                                if(tartomanyok[i][l+1] != tartomanyok[i][q] && tartomanyok[i][l-1] != tartomanyok[i][q]) {
                                    elfajultak[elfajult_meret][0] = tartomanyok[i][l]; elfajultak[elfajult_meret][1] = tartomanyok[i][q];
                                    elfajultak_tartomanya[elfajult_meret] = i+1;
                                    elfajult_meret++;
                                }
                            }
                        }

                    }
                }
            }



            if(elfajult_meret == 0) System.out.println("-nincsenek elfajult hidak.");
            else {
                System.out.println("-elfajultak:");
                for (int i = 0; i < elfajult_meret; i++) {
                    System.out.print("f"+elfajultak_tartomanya[i] + ": ");
                    for (int l = 0; l < 2; l++) {
                        System.out.print(elfajultak[i][l] + " ");
                    }
                    System.out.println();
                }
            }
    //elfajuló hidak keresése



    // F(B, Gact) meghatározása minden B hídra
            hidaktartomanyai = new int [V][max_tart_szam];
            hidaktartomanyainaksorszama = new int [V];
            hidaktartomanyainakszama = new int[V];

            for(int i=0; i<tart_szam; i++) {
                for(int m=0; m < G_Gact.getHidakszama(); m++) {
                    hidelemszam =0;
                    for(int n=0; n < G_Gact.getHidlabakhossza()[m]; n++) {
                        for (int x = 0; x < tel_tartomanyok[i]; x++) {
                            if (G_Gact.getHidlabak()[m][n] == tartomanyok[i][x]) hidelemszam++;
                        }
                    }
                    if (hidelemszam == G_Gact.getHidlabakhossza()[m]) {
                        hidaktartomanyai[m][hidaktartomanyainaksorszama[m]] = i+1;
                        hidaktartomanyainaksorszama[m]++;
                    }
                }
            }

            System.out.print("-F()-ek:\nF(B1,G"+hanyadikG+")={");
            for (int m = 0; m < G_Gact.getHidakszama(); m++) {
                for (int i = 0; i < max_tart_szam; i++) {
                    if(hidaktartomanyai[m][i]!=0 ) {
                        hidaktartomanyainakszama[m]++;
                        System.out.print("f" + hidaktartomanyai[m][i] + ", ");
                    }
                }
                System.out.println("}");
                if(m < G_Gact.getHidakszama()-1)System.out.print("F(B"+ (m+2)+",G"+hanyadikG+")={");
            }
            System.out.println();

            kivalasztott = -1;
            rajzolhatatlan = 0;
            for (int m = 0; m < G_Gact.getHidakszama(); m++) {
                if(hidaktartomanyainakszama[m] == 0 && elfajult_meret == 0) rajzolhatatlan++;
                if(hidaktartomanyainakszama[m] == 1) {
                    kivalasztott = m;
                    System.out.println("F(B" +(m+1)+ ", G"+hanyadikG+")-ből:");
                    System.out.print("f"+hidaktartomanyai[m][0] + ": ");
                    for(int i=0; i<tel_tartomanyok[hidaktartomanyai[m][0]-1];i++) {
                        System.out.print(tartomanyok[hidaktartomanyai[m][0]-1][i] +" ");
                    }
                    for (int i = 0; i < max_tart_szam; i++) {
                        if(hidaktartomanyai[m][i]!=0 )hidaktartomanyainakszama[m]++;
                    }
                    System.out.println();
                    break;
                }
            }
            //rajolhatatlan > 0 ?
            if(rajzolhatatlan == G_Gact.getHidakszama() && rajzolhatatlan != 0) {
                System.out.println("nem síkgráf");
                return;
            }
            if(kivalasztott == -1) {
                if(elfajult_meret > 0) {

                } else {
                    kivalasztott = 0;


                    System.out.println("F(B" +(0+1)+ ", G"+hanyadikG+")-ből:");
                    System.out.print("f"+hidaktartomanyai[0][0] + ": ");
                    for(int i=0; i<tel_tartomanyok[hidaktartomanyai[0][0]-1];i++) {
                        System.out.print(tartomanyok[hidaktartomanyai[0][0]-1][i] +" ");
                    }
                    for (int i = 0; i < max_tart_szam; i++) {
                        if (hidaktartomanyai[0][i] != 0) hidaktartomanyainakszama[0]++;
                    }
                }
            }
    // F(B, Gact) meghatározása minden B hídra


    // út megtalálása
            if(kivalasztott != -1) {
                A_hid = G_Gact.getHidak()[kivalasztott];
                A_hid_hossza = G_Gact.getHidakhossza()[kivalasztott];
                A_hid_labak = G_Gact.getHidlabak()[kivalasztott];
                A_hid_labak_hossza = G_Gact.getHidlabakhossza()[kivalasztott];
            }

            voltmar = new int[V];
            for(int i=0; i<V; i++) {
                voltmar[i] = 0;
            }
            if(kivalasztott !=-1) {
                kezd = A_hid_labak[0];
                veg = 0;
                path = new int[V];
                tel_path = 0;
            } else {
                kezd = elfajultak[0][0];
                veg = elfajultak[0][1];
                path = new int[V];
                tel_path = 0;
            }


            path[0] = kezd; tel_path++;

            if(kivalasztott == -1) {
                path[1] = veg; tel_path++;
            } else {
                for (int i = 0; i < hidelem_incidence.length; i++) {
                    hidelem_incidence[i] = 0;
                }

                int[] nemszabad = new int[V];
                for (int i = 0; i < V; i++) {
                    if (input[kezd - 1][i] == 1) {
                        for (int l = 0; l < G_act.length; l++) {
                            if (i + 1 == G_act[l]) {
                                nemszabad[i] = 1;
                            }
                        }
                    }
                }

                nemlehet = new int[2];
                for (int k = 0; k < tel_tartomanyok[hidaktartomanyai[kivalasztott][0] - 1]; k++) {
                    if (tartomanyok[hidaktartomanyai[kivalasztott][0] - 1][k] == kezd) {
                        if (k != 0 && k != tel_tartomanyok[hidaktartomanyai[kivalasztott][0] - 1] - 1) {
                            nemlehet[0] = tartomanyok[hidaktartomanyai[kivalasztott][0] - 1][k - 1];
                            nemlehet[1] = tartomanyok[hidaktartomanyai[kivalasztott][0] - 1][k + 1];
                        } else if (k == 0) {
                            nemlehet[0] = tartomanyok[hidaktartomanyai[kivalasztott][0] - 1][tel_tartomanyok[hidaktartomanyai[kivalasztott][0] - 1] - 1];
                            nemlehet[1] = tartomanyok[hidaktartomanyai[kivalasztott][0] - 1][k + 1];
                        } else {
                            nemlehet[0] = tartomanyok[hidaktartomanyai[kivalasztott][0] - 1][k - 1];
                            nemlehet[1] = tartomanyok[hidaktartomanyai[kivalasztott][0] - 1][0];
                        }
                    }
                }

                jel = 0;

                int next = 0;

                for (int i = 0; i < V; i++) {
                    if (input[path[0] - 1][i] == 1 && voltmar[i] == 0 && i != (kezd - 1)) {

                        if (nemszabad[i] == 1) continue;
                        if (i + 1 != nemlehet[0] && i + 1 != nemlehet[1]) {
                            path[1] = i + 1;
                            tel_path++;
                            voltmar[i] = 1;
                            for (int l = 0; l < A_hid_labak_hossza; l++) {
                                if (i == A_hid_labak[l] - 1) {
                                    veg = path[1];
                                    jel++;
                                }
                            }
                            break;
                        }
                    }
                }


                for (int m = 1; m < V; m++) {
                    if (jel != 0) break;
                    for (int i = 0; i < V; i++) {
                        if (input[path[m] - 1][i] == 1 && voltmar[i] == 0 && i != (kezd - 1)) {
                            path[m + 1] = i + 1;
                            tel_path++;
                            voltmar[i] = 1;

                            for (int l = 0; l < A_hid_labak_hossza; l++) {
                                if (i == A_hid_labak[l] - 1) {
                                    veg = path[m + 1];
                                    jel++;
                                }
                            }
                            break;
                        }
                    }
                    if (jel != 0) break;
                }
            }

            System.out.println("Az út: ");
            for(int i=0; i< tel_path; i++) {
                if(i < tel_path-1) {
                    elek[path[i]-1][path[i+1]-1] = 1;
                    elek[path[i+1]-1][path[i]-1] = 1;
                    rajz[path[i]-1][path[i+1]-1] = 1;
                }
                System.out.print(path[i] + " ");
            }
    // út megtalálása


    // G_next létrehozása
            G_next = new int[V];
            int tel_Gnext=0;
            for(int i=0; i < tel_Gact; i++) {
                G_next[i] = G_act[i]; tel_Gnext++;
            }
            for(int i=1; i < tel_path-1;i++) {
                G_next[tel_Gnext] = path[i]; tel_Gnext++;
            }

            System.out.println("\n______________________________________\n");
            System.out.println("G"+(hanyadikG+1)+":\n");
            for(int i=0; i<tel_Gnext; i++) {
                //System.out.print(G_next[i] + " ");
            }
    // G_next létrehozása


    // tartományok frissítése
            tartmasolat = new int[V];
            if (kivalasztott == -1) {
                for (int i = 0; i < tel_tartomanyok[elfajultak_tartomanya[0]-1]; i++) {
                    tartmasolat[i] = tartomanyok[elfajultak_tartomanya[0] -1][i];
                }
                tel_tartmasolat = tel_tartomanyok[elfajultak_tartomanya[0] - 1];

                tel_tartomanyok[elfajultak_tartomanya[0] - 1] = 0;
                for (int i = 0; i < tartomanyok[elfajultak_tartomanya[0] - 1].length; i++) {
                    tartomanyok[elfajultak_tartomanya[0] - 1][i] = 0;
                }
                for (int i = tel_path - 1, l = 0; i >= 0 && l < tel_path; i--, l++) {
                    tartomanyok[elfajultak_tartomanya[0] - 1][l] = path[i];
                    tel_tartomanyok[elfajultak_tartomanya[0] - 1]++;
                }
                vegevan = 0;
                index = tel_tartmasolat - 1;
                while (vegevan == 0) {
                    if (tartmasolat[index] == kezd) {
                        int l = 0;
                        while (vegevan == 0) {
                            if (index > 0) {
                                index--;
                            } else {
                                index = tel_tartmasolat - 1;
                            }
                            if (tartmasolat[index] == veg) {
                                vegevan++;
                                break;
                            }
                            tartomanyok[elfajultak_tartomanya[0] - 1][tel_path + l] = tartmasolat[index];
                            tel_tartomanyok[elfajultak_tartomanya[0] - 1]++;
                            l++;
                        }
                    }
                    if (index > 0) {
                        index--;
                    } else {
                        index = tel_tartmasolat - 1;
                    }
                }


                vegevan = 0;
                index = 0;
                ujtart_sorszam = tart_szam;

                for (int i = tel_path - 1, l = 0; i >= 0 && l < tel_path; i--, l++) {
                    tartomanyok[ujtart_sorszam][l] = path[i];
                    tel_tartomanyok[ujtart_sorszam]++;
                }
                while (vegevan == 0) {
                    if (tartmasolat[index] == kezd) {
                        int l = 0;
                        while (vegevan == 0) {
                            if (index < tel_tartmasolat - 1) {
                                index++;
                            } else {
                                index = 0;
                            }
                            if (tartmasolat[index] == veg) {
                                vegevan++;
                                break;
                            }
                            tartomanyok[ujtart_sorszam][tel_path + l] = tartmasolat[index];
                            tel_tartomanyok[ujtart_sorszam]++;
                            l++;
                        }
                    }
                    if (index < tel_tartmasolat - 1) {
                        index++;
                    } else {
                        index = 0;
                    }
                }
                tart_szam++;

            } else {
                for (int i = 0; i < tartmasolat.length; i++) {
                    tartmasolat[i] = tartomanyok[hidaktartomanyai[kivalasztott][0] - 1][i];
                }
                tel_tartmasolat = tel_tartomanyok[hidaktartomanyai[kivalasztott][0] - 1];

                tel_tartomanyok[hidaktartomanyai[kivalasztott][0] - 1] = 0;
                for (int i = 0; i < tartomanyok[hidaktartomanyai[kivalasztott][0] - 1].length; i++) {
                    tartomanyok[hidaktartomanyai[kivalasztott][0] - 1][i] = 0;
                }
                for (int i = tel_path - 1, l = 0; i >= 0 && l < tel_path; i--, l++) {
                    tartomanyok[hidaktartomanyai[kivalasztott][0] - 1][l] = path[i];
                    tel_tartomanyok[hidaktartomanyai[kivalasztott][0] - 1]++;
                }
                vegevan = 0;
                index = tel_tartmasolat - 1;
                while (vegevan == 0) {
                    if (tartmasolat[index] == kezd) {
                        int l = 0;
                        while (vegevan == 0) {
                            if (index > 0) {
                                index--;
                            } else {
                                index = tel_tartmasolat - 1;
                            }
                            if (tartmasolat[index] == veg) {
                                vegevan++;
                                break;
                            }
                            tartomanyok[hidaktartomanyai[kivalasztott][0] - 1][tel_path + l] = tartmasolat[index];
                            tel_tartomanyok[hidaktartomanyai[kivalasztott][0] - 1]++;
                            l++;
                        }
                    }
                    if (index > 0) {
                        index--;
                    } else {
                        index = tel_tartmasolat - 1;
                    }
                }


                vegevan = 0;
                index = 0;
                ujtart_sorszam = tart_szam;

                for (int i = tel_path - 1, l = 0; i >= 0 && l < tel_path; i--, l++) {
                    tartomanyok[ujtart_sorszam][l] = path[i];
                    tel_tartomanyok[ujtart_sorszam]++;
                }
                while (vegevan == 0) {
                    if (tartmasolat[index] == kezd) {
                        int l = 0;
                        while (vegevan == 0) {
                            if (index < tel_tartmasolat - 1) {
                                index++;
                            } else {
                                index = 0;
                            }
                            if (tartmasolat[index] == veg) {
                                vegevan++;
                                break;
                            }
                            tartomanyok[ujtart_sorszam][tel_path + l] = tartmasolat[index];
                            tel_tartomanyok[ujtart_sorszam]++;
                            l++;
                        }
                    }
                    if (index < tel_tartmasolat - 1) {
                        index++;
                    } else {
                        index = 0;
                    }
                }
                tart_szam++;
            }
            System.out.println("-tartományok:");
            for(int i = 0; i < tart_szam; i++) {
                System.out.print( "f"+ (i + 1) + ": ");
                for(int l = 0; l < tel_tartomanyok[i]; l++)System.out.print(tartomanyok[i][l] + " ");
                System.out.println();
            }
    // tartományok frissítése


    // G_next illeszkedési mátrixának létrehozása
            int[][] incidenceGnext = new int[V][V];
            for(int i = 0; i < V; i++) {
                for(int l = 0; l < V; l++) {
                    if(input[i][l]==1)incidenceGnext[i][l] = 1;
                }
            }
            int[] Gact_masolat = new int[V];
            for(int i=0; i<G_act.length; i++) {
                Gact_masolat[i] = G2[i];
            }
            int[] reverseGnext = new int[V];
            for(int i=0; i<G_next.length; i++) {
                if(G_next[i]!=0)reverseGnext[G_next[i]-1] = 1;
            }
            for(int i= 0; i < V; i++) {
                if(reverseGnext[i] == 0) {
                    for(int l=0; l<V; l++) {
                        incidenceGnext[l][i] = 0;
                        incidenceGnext[i][l] = 0;
                    }
                }
            }
            //System.out.println();
            //System.out.println("G" +hanyadikG+1+"illeszkedési mátrixa");
            for(int i = 0; i < incidenceGnext.length; i++) {
                for(int l = 0; l < incidenceGnext.length; l++) ;//System.out.print(incidenceGnext[l][i] + " ");
                //System.out.println();
            }
    // G_next illeszkedési mátrixának létrehozása




            G_Gact = G_Gprev;

            for(int i = 0; i < V; i++) {
                for(int l = 0; l < V; l++) {
                    if(G_Gact.getIncidence()[i][l]==1)G_Gact_masolat[i][l] = 1;
                }
            }
            for(int i = 0; i < V; i++) {
                for(int l = 0; l < V; l++) {
                    if(G_Gact_masolat[i][l]==1)G_Gprev_masolat[i][l] = 1;
                }
            }
            G_incidence_act = incidenceGnext;
            G_act = G_next;
            tel_Gact = tel_Gnext;

            rajzoltElek = rajzoltElek + tel_path-1;
            System.out.println("E:" + E + ", rajzoltelek:" + rajzoltElek);

            hanyadikG ++;

        }
// ciklus vége


        System.out.println("Síkgráf, mert sikerült lerajzolnunk az egész gráfot. :)");



    }
}