package game;

public class Item {
	public int row;
	public int col;

    public Item(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
    public boolean equals(Item item) {
    	return (item.getRow()==this.getRow()&&item.getCol()==this.getCol());
    }
}
