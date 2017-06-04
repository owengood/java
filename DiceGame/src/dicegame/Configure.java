package dicegame;

import diceService.DiceGame;

public class Configure {
	private DiceGame dicegame = null;
	public Configure(DiceGame dicegame){
		this.dicegame = dicegame;
	}
	
	public void setcell(int[] cell){
		dicegame.setcell(cell);
	}
	
	public int[] getcell(){
		return dicegame.getcell();
	}
}
