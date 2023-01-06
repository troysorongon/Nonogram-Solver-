//package Lab02.Nonogram;
//@Troy Sorongon
//@03/10/2022
//CS245 Lab02: Nonogram

import java.util.Arrays;

public class Nonogram {
	/**
	 * Method checks if the inputs for the row and columns satisfy the constraints for the Nonogram puzzle
	 * Constraints --> 1. 9x9 2. each row or column block set has at most 2 elements 
	 * @param int[][] theRows, the row block sets
	 * @param int[][] theColumns, the column block sets
	 * @throws IllegalArgumentException if puzzle does not satisfy the constraints
	 */
	public static void validBoard(int[][]theRows, int[][]theColumns) throws IllegalArgumentException
	{
		// checks if either the length of rows or columns are greater than 9
		if(theRows.length > 9 || theColumns.length > 9) 
		{	
			throw new IllegalArgumentException("Puzzle exceeds 9x9 matrix");	
		}
		
		// checks if row block set has more than 2 elements
		for(int i = 0; i < theRows.length; i++) 
		{	
			if(theRows[i].length > 2) 
			{
				throw new IllegalArgumentException("Row exceeds more than 2 blocks");
			}
		}
		
		// checks if column block set has more than 2 elements
		for(int j = 0; j < theColumns.length; j++) 
		{
			if(theColumns[j].length > 2) 
			{
				throw new IllegalArgumentException("Column exceeds more than 2 blocks");
			}
		}
	}
	
	
	/**
	 * 
	 * @param board boolean[][], puzzle is solved of length of rows and columns
	 * @param col int[][], the blocksets of the column
	 * @param rowLength int
	 * @param rowStart int, starting row position of blocks to be placed
	 * @param isComplete boolean, true if at the end of the row or else false
	 * @return true if board is safe to place blocks or else returns false
	 */
	public static boolean isSafe(boolean[][] board, int[][] col, int rowLength, int rowStart, boolean isComplete)
	{
		for(int c = 0; c < col.length; c++)
		{
			int blockset1 = col[c][0];
			int blockset2 = col[c][1];
			
			if(rowStart < blockset1 || rowStart < blockset2)
			{
				 continue;
			}
			
			int placedBlocks = 0;	// tracks the amount of consecutive blocks placed
			int row = 0;	// row index for board
			
			// checks for 1st set of blocks
			if(blockset1 != 0)
			{
				for(; row < rowLength; row++)
				{
					if(board[row][c] == true)
					{
						placedBlocks++;
					}
					else	// if board[row][c] == false
					{
						placedBlocks = 0;
					}
					
					if(placedBlocks == blockset1)
					{
						row++;
						if(board[row][c] == true)	// if next position on the board is true, it is filled and not a valid move
						{
							return false;	
						}
						else
						{
							break;
						}
					}
				}
				
				placedBlocks = 0;	// reset counter to check blockset2
			}
			
			// checks for 2nd set of blocks
			for(; row < rowLength; row++)
			{
				if(board[row][c] == true)
				{
					placedBlocks++;
				}
				else	// if board[row][c] == false
				{
					placedBlocks = 0;
				}
				
				if(placedBlocks == blockset2)
				{
					row++;
					break;
				}
			}
			
			// checks if the remainder of the column is filled
			for(; row < rowLength; row++)	
			{
				if(board[row][c] == true)
				{
					return false;
				}
			}
			
			// checks if current block is at the end of the row && placedBlocks haven't yet matched blockset2
			if(isComplete && placedBlocks != blockset2) 
			{
				return false;
			}
			
		}
		
		return true;
	}
	
	
	private static void placeBlocks(int startRow, int startCol, int numBlocks, boolean[][] board)
	{
		for(int c = 0; c < numBlocks; c++)
		{
			board[startRow][startCol + c] = true;
		}
	}
	
	
	private static void removeBlocks(int startRow, int startCol, int numBlocks, boolean[][] board)
	{
		for(int c = 0; c < numBlocks; c++)
		{
			board[startRow][startCol + c] = false;
		}
	}
	
	
	public static boolean solve(boolean[][] board, int[][] rows, int[][] cols, int rowStart, int colStart, boolean idxZero)
	{
		// if the board is not safe to place blocks
		if(!isSafe(board, cols, rows.length, rowStart, false))
		{
			return false;
		}
		// if at the end of the row 
		else if(rowStart == rows.length)
		{
			return isSafe(board, cols, rows.length, rowStart, true);	// checks if blocks are placed correctly
		}
		else
		{
			int num;
			
			if(idxZero == true && rows[rowStart][0] != 0)
			{
				num = rows[rowStart][0] + rows[rowStart][1];
				
			}
			else
			{
				num = colStart + rows[rowStart][1] - 1;
			}
			
			int cond = cols.length - num;
			
			for(int i = 0; i < cond; i++)
			{
				boolean placed;
				int idx;
				if(idxZero)
				{
					idx = 0;
				}
				else 
				{
					idx = 1;
				}
				
				int numBlocks = rows[rowStart][idx];
				
				placeBlocks(rowStart, colStart + i, numBlocks, board);
				
				if(idxZero == true)
				{
					int colIdx = 0;
					if(rows[rowStart][0] != 0)
					{
						colIdx = rows[rowStart][0] + 1 + i;
					}
					placed = solve(board, rows, cols, rowStart, colStart + colIdx, false);
				}
				else
				{
					placed = solve(board, rows, cols, rowStart + 1, 0, true);
				}
				
				if(placed)
				{
					return true; // solved the puzzle
				}
				
				idx = 1;
				if(idxZero)
				{
					idx = 0;
				}
				numBlocks = rows[rowStart][idx];
				removeBlocks(rowStart, colStart + i, numBlocks, board);
			}
		}
		
		return false; // was not able to be solved
	}
	
	
	public static boolean[][] solveBoard(int[][] rows, int[][] cols)
	{
		validBoard(rows, cols);
		boolean[][] board = new boolean[rows.length][cols.length];
		solve(board, rows, cols, 0, 0, true);
		return board;
	}
	
	
	public static void printBoard(int[][] rows, int[][] cols, boolean[][] board)
	{
		System.out.print("    ");
		for(int[] col : cols)
		{
			System.out.print(col[0] + " ");
		}
		
		System.out.println();
		System.out.print("    ");
		
		for(int[] col : cols)
		{
			System.out.print(col[1] + " ");
		}
		
		System.out.println();
		
		char block;
		for(int r = 0; r < rows.length; r++)
		{
			System.out.print(rows[r][0] + " " + rows[r][1] + " ");
			
			for(int c = 0; c < board[0].length; c++)
			{
				if(board[r][c] == true)
				{
					block = 'O';
					
				}
				else
				{
					block = 'X';
				}
				
				System.out.print(block + " ");
			}
			System.out.println();
		}
	}
	
	// Testing
	public static void main(String[] args){
		int[][] rows = {{4,3}, {3,4}};
		int[][] columns = {{0,2}, {0,2}, {0,2}, {0,1}, {0,1}, {0,1}, {0,2}, {0,2}, {0,1}};;
		boolean[][] board = solveBoard(rows, columns);
		printBoard(rows, columns, board);
	}
}


