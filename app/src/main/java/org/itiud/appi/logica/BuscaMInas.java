package org.itiud.appi.logica;

public class BuscaMInas {
    protected static final String MINE = "\uD83D\uDCA3";
    public static final String WAIT = " ";
    private static final int num = 10;
    private boolean end;
    private boolean start;
    private Celda[][] matrix = new Celda[8][8];
    private Celda celda;

    public void fill() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                celda = new Celda();
                matrix[i][j] = celda;
            }
        }
    }
    public boolean isEnd() {
        return end;
    }

    public Celda[][] play(int x, int y) {
        if (start) {
            eval(x, y);
            mining();
            putNums();
            revealing(x, y);
        } else {
            if (!this.matrix[x][y].getValue().equals(MINE)) {
                Stry(x, y);

            } else {
                this.matrix[x][y].setEstado(false);
                bomb_();
                this.end = false;
            }
        }
        return this.matrix;
    }
    private void bomb_() {
        for(int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                if(this.matrix[i][j].getValue().equals(MINE)){
                    this.matrix[i][j].setEstado(false);
                }
            }
        }
    }
    public boolean win(){
        int cuenta=0;
        for(int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                if(this.matrix[i][j].isEstado() && !this.matrix[i][j].getValue().equals(MINE)){
                    cuenta++;
                }
            }
        }
        return cuenta==(0);
    }
    private void mining() {
        int mining=0;
        while(mining!=num){
            {
                int X = (int) (Math.random() * 8);
                int Y = (int) (Math.random() * 8);
                if ((!this.matrix[X][Y].getValue().equals(MINE)) && !this.matrix[X][Y].getValue().equals(WAIT)){
                    celda = new Celda(true, MINE);
                    this.matrix[X][Y] = celda;
                    mining++;
                }
            }
        }
    }
    private void putNums() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (!(this.matrix[i][j].getValue().equals(MINE)) )  {
                    int count = 0;
                    if (i > 0 && j > 0 && this.matrix[i - 1][j - 1].getValue().equals(MINE)) {
                        count++;
                    }
                    if (i > 0 && this.matrix[i - 1][j].getValue().equals(MINE)) {
                        count++;
                    }
                    if (i > 0 && j < 7 && this.matrix[i - 1][j + 1].getValue().equals(MINE)) {
                        count++;
                    }
                    if (j > 0 && this.matrix[i][j - 1].getValue().equals(MINE)) {
                        count++;
                    }
                    if (j < 7 && this.matrix[i][j + 1].getValue().equals(MINE)) {
                        count++;
                    }
                    if ((i < 7 && j > 0) && this.matrix[i + 1][j - 1].getValue().equals(MINE)) {
                        count++;
                    }
                    if (i < 7 && this.matrix[i + 1][j].getValue().equals(MINE)) {
                        count++;
                    }
                    if (i < 7 && j < 7 && this.matrix[i + 1][j + 1].getValue().equals(MINE)) {
                        count++;
                    }
                    Celda num = new Celda(true, count+"");
                    this.matrix[i][j] = num;
                }
            }
        }
    }
    public void revealing(int x, int y) {
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (0 <= i && i < 8
                        && 0 <= j && j < 8
                        && !(i == x && j == y)
                        && this.matrix[i][j].getValue().equals("0")) {
                    keepRevealing(i, j);
                }
                else if (0 <= i && i < 8
                        && 0 <= j && j < 8
                        && !(i == x && j == y)
                        && !this.matrix[i][j].getValue().equals("0")){
                    this.matrix[i][j].setEstado(false);
                }
            }
        }
        this.matrix[x][y].setEstado(false);
    }
    private void keepRevealing(int x, int y) {
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (0 <= i && i < 8
                        && 0 <= j && j < 8
                        && !(i == x && j == y)
                        && (this.matrix[i][j].isEstado())
                        && !this.matrix[i][j].getValue().equals(MINE)
                        && this.matrix[i][j].getValue().equals("0")) {
                    this.matrix[i][j].setEstado(false);
                    showNums(i, j);
                    keepRevealing(i, j);

                }
            }
        }
    }
    private void Stry(int x, int y) {
      if(!(Integer.parseInt(String.valueOf(this.matrix[x][y].getValue()))>0)) {
            for (int i = x - 1; i <= x + 1; i++) {
                for (int j = y - 1; j <= y + 1; j++) {
                    if (0 <= i && i < 8
                            && 0 <= j && j < 8
                            && (this.matrix[i][j].isEstado())
                            && !this.matrix[i][j].getValue().equals(MINE))
                    {
                        {
                            this.matrix[i][j].setEstado(false);
                            Stry(i, j);
                        }
                    }
                }
            }
      }else{
         this.matrix[x][y].setEstado(false);
      }
    }
    private void showNums ( int x, int y){
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (0 <= i && i < 8
                        && 0 <= j && j < 8
                        && (Integer.parseInt(String.valueOf(this.matrix[i][j].getValue()))>0))
                {
                    this.matrix[i][j].setEstado(false);

                }
            }
        }
    }
    private void eval ( int i, int j){
        celda= new Celda(true, WAIT);
        for(int x=i-1;x<=i+1;x++){
            for(int y=j-1;y<=j+1;y++){
                if( 0<=x &&  x < 8 &&
                        0<=y &&  y < 8){
                    this.matrix[x][y] = celda;
                }
            }
        }
        this.start=false;
    }
    public Celda[][] getMatrix () {
        return matrix;
    }

    public int GoodCells(){
        int count =0;
        for(int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                if(!this.matrix[i][j].isEstado() && !this.matrix[i][j].getValue().equals(MINE)){
                    count++;
                }
            }
        }
        return count;
    }

    public BuscaMInas() {
        this.start=true;
        this.end = true;
        fill();
    }
}