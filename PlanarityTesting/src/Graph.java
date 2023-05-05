
public class Graph {
    private int[][] incidence; // illeszkedési mátrix
    private int[] value; // értéktömb, a keresésekhez (az i. csúcshoz a value tömb i. sorszámán lévő érték tartozik)
    private int n; // mélységi keresés során használt sorszám
    private int[] cycle; // ebben a változóban tároljuk a körünket, az úttal együtt amelyen megtaláltuk
    private int k; // a cycle tömb tellítettségét jelző változó
    private int jel; // ezzel jelezzük hogy megtaláltuk a körünket(rekurzív eljárás megtöréséhez kell)
    private int[] voltmar; // find_component egy változója (jelzi hogy az i-edik csúcs már előfordult - ennek
    //segítségével tudjuk hogy melyik sort(oszolopot) kell lenullázni)
    private int[] uressorok; // ez a tömb jelzi, hogy az illeszkedési mátrix mely sorai(oszlopai) csupa nullák

    private int[][] hidak;
    private int[][] hidlabak;
    private int[] hidlabakhossza;
    private int hidakszama;
    private int hidakhossza[];

    public Graph(int[][] incidence) {           // konstruktor
        this.incidence = incidence;
        value = new int[incidence.length];
        cycle = new int[incidence.length+1];
        voltmar = new int[incidence.length];
        uressorok = new int[incidence.length];
        hidak = new int[incidence.length][incidence.length];
        hidlabak = new int[incidence.length][incidence.length];
        hidakhossza = new int[incidence.length];
        hidlabakhossza = new int[incidence.length];
        for(int i=0; i < incidence.length; i++) {
            voltmar[i] = 0;
            uressorok[i] = 0;
            hidakhossza[i] = 0;
            hidlabakhossza[i] = 0;
        }
        n = 0;
        jel = 1;
    }


// Getterek és Szetterek
    public int[][] getIncidence() {
        return incidence;
    }
    public void setIncidence(int[][] incidence) {
        this.incidence = incidence;
    }
    public int[] getValue() {
        return value;
    }
    public void setValue(int[] value) {
        this.value = value;
    }
    public int getN() {
        return n;
    }
    public void setN(int n) {
        this.n = n;
    }
    public int[] getCycle() {
        return cycle;
    }
    public int getK() {
        return k;
    }
    public void setK(int k) {
        this.k = k;
    }
    public int[] getVoltmar() {return voltmar;}
    public int[] getUressorok() {
        return uressorok;
    }
    public int[][] getHidak() {return hidak;}
    public int getHidakszama() {
        return hidakszama;
    }
    public int[][] getHidlabak() {
        return hidlabak;
    }
    public int[] getHidakhossza() {
        return hidakhossza;
    }
    public int[] getHidlabakhossza() {return  hidlabakhossza;}
    // Getterek és Szetterek


    public int[] find_cycle(int v, int u) {
        n++; value[v] = n;
        for(int i = 0; i < incidence[v].length; i++) {
            if(incidence[v][i] == 1) {
                if(value[i] == 0) {
                    k++; cycle[k] = i+1;
                    if(uressorok[i] == 0) find_cycle(i, v);
                    if(jel == 0) break;
                } else if(value[i] < value[v] && value[u] != value[i]) {
                    k++; cycle[k] = i+1; jel = 0; return cycle;
                }
            }
        }
        return cycle;
    }


    public int[] find_component(int v, int u) { // tetszőleges G gráf maximális-összefüggő-részgráfjait keresi meg.
        n++;
        value[v] = n;
        int ures = 0;
        if( u == 0 && v != 0) {
            int elsoszamlalo = 0;
            for (int i = 0; i < incidence[v].length; i++) {
                if (incidence[v][i] == 1 && value[i] == 0) {
                    elsoszamlalo++;
                }
            }
            if (elsoszamlalo == 0) {
                return cycle;
            }
        }
        for (int i = 0; i < incidence[v].length; i++) {
            if (incidence[v][i] == 1) {
                if (value[i] == 0) {
                    k++;
                    cycle[k] = i + 1;
                    voltmar[i] = 1;
                    find_component(i, v);
                }
            }
        }
        return cycle;
    }

    public void find_components(int[] lehet_hidelem) {
        int v = 0;
        int uressorokszama = 0;
        int[] C;
        hidakszama = 0;
        int jo = 0;

        int ures;
        for(int i=0; i < this.getIncidence().length; i++) {
            ures = 0;
            for(int l=0; l < this.getIncidence().length; l++) {
                if(this.getIncidence()[i][l] == 0) ures++;
            }
            if(ures == this.getIncidence().length) this.getUressorok()[i] = 1;
        }
        uressorokszama = 0;
        for(int i=0;i < this.getUressorok().length;i++) {
            if(this.getUressorok()[i] == 1) uressorokszama++;
        }
        int esely = 0;
        for(int i=0; i<lehet_hidelem.length; i++) {
            if(lehet_hidelem[i] == 1) esely++;
        }
        do {
            if(uressorokszama == this.getIncidence().length && esely==0)break;
            for(int i= 0; i < this.getIncidence().length; i++) this.getValue()[i] = 0;
            for(int i= 0; i < this.getIncidence().length; i++) {
                if(lehet_hidelem[i] == 1 && this.getVoltmar()[i] == 0) {
                    v = i;
                    break;
                }
            }
            this.setK(0);
            for(int i= 0; i < this.getCycle().length; i++) this.getCycle()[i] = 0;
            this.getCycle()[0] = v+1;
            this.getVoltmar()[v] = 1;
            hidakszama++;
            C = this.find_component(v, 0);
            for(int i= 0; i< this.getHidak().length; i++) {
                this.getHidak()[hidakszama-1][i] = C[i];
                if(C[i]!=0)if(lehet_hidelem[C[i]-1] == 1)lehet_hidelem[C[i]-1] = 0;
                if(C[i] != 0) hidakhossza[hidakszama-1]++;
            }
            for(int i=0;i<this.getIncidence().length; i++) {
                if(this.getVoltmar()[i] == 1) {
                    for(int l=0; l< this.getIncidence().length; l++) {
                        this.getIncidence()[i][l] =0;
                        this.getIncidence()[l][i] =0;
                    }
                }
            }

            for(int i=0; i < this.getIncidence().length; i++) {
                ures = 0;
                for(int l=0; l < this.getIncidence().length; l++) {
                    if(this.getIncidence()[i][l] == 0) ures++;
                }
                if(ures == this.getIncidence().length) this.getUressorok()[i] = 1;
            }
            uressorokszama = 0;
            for(int i=0;i < this.getUressorok().length;i++) {
                if(this.getUressorok()[i] == 1) uressorokszama++;
            }

            esely = 0;
            for(int i=0; i<lehet_hidelem.length; i++) {
                if(lehet_hidelem[i] == 1) esely++;
            }

        } while(uressorokszama != this.getIncidence().length || esely !=0);
    }
}
