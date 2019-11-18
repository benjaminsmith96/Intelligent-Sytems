import javax.swing.JOptionPane;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/*
Name: Ben Smith
ID: 14160668
*/

public class ISFinal{

	public static void main(String[] args) {
		
		String varType[] = {"G","P","S","E","C","D","Cr","Mu"};
		int G = checkInput("Please enter the number of Generations", varType[0], 0);
		int P = checkInput("Please enter Population size", varType[1], 0);
		int S = checkInput("Please enter number of Students", varType[2], 0);
		int E = checkInput("Please enter number of Modules", varType[3], 0);
		int C = checkInput("Please enter number of modules on the course", varType[4], E);
		int D = checkInput("Please enter number of exam sessions/days", varType[5], 0);
		int Cr = checkInput("Please enter Crossover probability", varType[6], 0);
		int Mu = checkInput("Please enter Mutation probability", varType[7], 0);

		int[][] popMod = new int[P][E];
		int[][] studentTable = new int[S][C];
		int[] fitnessArray = new int [P];
		int[][] sortedPopMod = new int [P][E];
		int[][] newPopMod = new int [P][E];
		int[][] geneticMod = new int [P][E];
		Scanner keyIn = new Scanner(System.in);
		
		generateTable(S, C, E, studentTable);
		
		for (int y = 0; y < S; y++){
			System.out.print("Student " + (y+1) + ": ");
			for(int z = 0;z < C;z++){
				System.out.print(studentTable[y][z] + " ");
			}
			System.out.println();
	    }
		System.out.println();
		keyIn.nextLine();
		keyIn.close();
		
		for(int i = 0; i<G;i++)
		{
			generatePopulation(P, E, popMod);
		
			fitnessCost(D, E, popMod, studentTable, fitnessArray, G, 2, i);//2 WAS 0!!!!!!!
		
			System.out.println();
			sortByFitness(popMod, fitnessArray, sortedPopMod);
		
			divideArray(sortedPopMod, newPopMod);
		
			goCMR(newPopMod, geneticMod, Cr, Mu);
		
			fitnessCost(D, E, geneticMod, studentTable, fitnessArray, G, 1, i);
		}

	}
	/* Checks input from JOption to see if its an integer throws error if not */
	public static int checkInput(String message, String varType, int EStore)
	{
		String input;
		int var = 0;
		boolean inputAccepted = false;
		do{
			input = JOptionPane.showInputDialog(message);
			try{
				var = Integer.parseInt(input);
			}
			catch (NumberFormatException e){
				JOptionPane.showMessageDialog(null, "Integer values only", "Error", 0);
				inputAccepted = false;
			}
			if(var < 0){
				inputAccepted = false;
				JOptionPane.showMessageDialog(null, "Positive Integer values only", "Error", 0);
			}
			else if(var > 0){
				inputAccepted = true;
			}
			
			if(varType == "C" && EStore < var){
				JOptionPane.showMessageDialog(null, "The Number of modules in the course must be less than the total number of modules.", "Error", 0);
				inputAccepted = false;
			}

		}while(!inputAccepted);
			
		return var;
	}

	public static void generatePopulation(int P, int E, int[][] multi)
	{
		int[] anArray = new int[E];
		int arrayNumInput = 1;
		for (int i = 0; i < E; i++)
		{
			anArray[i] = arrayNumInput;
			arrayNumInput++;
		}
		
		for (int i = 0; i < multi.length; i++)
	    {
			shufflePop(anArray);
			for(int x = 0;x < anArray.length;x++)
			{
				multi[i][x] = anArray[x];
			}
	    }
		compareArrayRows(multi);
	}
	
	public static void shufflePop(int [] ar)
	{
		Random rnd = ThreadLocalRandom.current();
		for (int i = ar.length - 1; i > 0; i--)
		{
			int index = rnd.nextInt(i + 1);
			// Simple swap
			int a = ar[index];
			ar[index] = ar[i];
			ar[i] = a;
		}		
	}
	
	public static void compareArrayRows(int arr[][])
	{
		int[] tempArray = new int[arr[0].length];
		for(int x = 0;x < arr.length; x++)
		{
			for(int y = 0;y < arr[0].length;y++)
			{
				tempArray[y] = arr[x][y];
			}
			
			for(int z = 0 ; z < arr.length ; z++)
			{
				if(x != z && Arrays.equals(arr[z], tempArray))
				{
					shufflePop(tempArray);
					for(int b = 0;b < arr[0].length;b++)
					{
						arr[x][b] = tempArray[b];
					}
					z = -1;
				}
			}
		}
	}

	public static void generateTable(int S, int C,int length, int[][] studentTable)
	{
		int[] tempArray = new int [studentTable[0].length];
		
		for(int x = 0;x < studentTable.length; x++)
		{
			shuffleTable(tempArray, length);
			for(int y = 0;y < studentTable[0].length;y++)
			{
				studentTable[x][y] = tempArray[y];
			}
		}
	}
	
	public static void shuffleTable(int[] tempArray, int length)
	{
		int[] array = new int[length];
		for(int a = 0; a < length; a++)
		{
			array[a] = a + 1;
		}
		int index;
	    Random random = new Random();
	    int x = 0;
	    while(x < tempArray.length)
	    {
	    	index = random.nextInt(array.length);
	        if(array[index] != 0)
	        {
	        	tempArray[x] = array[index];
	        	array[index] = 0;
	        	x++;
	        }
	    }
	}
	
	public static void fitnessCost(int D, int E, int[][] pop, int[][] table, int[] fitnessArray, int G, int type,int GenN0)
	{	
		int result = (int) Math.ceil((double) E / D);
		int fitnessCost;
		int match;
		int[] tempA = new int[result];
		int temp;
		int re = D * result;
		int remain = re - E;
		if(type == 0)
			System.out.println("Population\n");
		int p = 0;
		while(p < pop.length)//pop row
		{		
			if(type == 0)
				System.out.print("Orderin "+ (p+1) +": ");
			for(int e = 0; e < pop[0].length ; e++){
				if(type == 0)
					System.out.print(pop[p][e] + " ");
			}
			if(type == 0)
				System.out.print(": Fitness Cost: ");
			int e = 0;
			temp=0;
			fitnessCost = 0;
			while(e < pop[0].length + remain && temp < tempA.length)//pop column
			{
				if(e<pop[0].length)
					tempA[temp] = pop[p][e];
				else if(e<pop[0].length + remain)
					tempA[temp] = 0;
				if(temp == result - 1)
				{
					for(int s = 0; s < table.length; s++)//student row
					{
						match = 0;
						for(int c = 0; c < table[0].length; c++)//student column
						{
							for(int x = 0; x<tempA.length;x++)
							{
								if(table[s][c]==tempA[x])
								{
									match++;
								}
							}	
						}
						if(match > 1)
						{
							fitnessCost++;
						}
					}
				}
				temp++;
				if(temp == result){
					temp = 0;
				}
				e++;
			}
			fitnessArray[p] = fitnessCost;
			if(type == 0)
				System.out.println(fitnessCost);
			p++;
		}
		if(type == 1)
		{
			int minValue = fitnessArray[0];
		    for (int i = 1; i < fitnessArray.length; i++) {
		        if (fitnessArray[i] < minValue) {
		            minValue = i;
		        }
		    }
		    printGen(fitnessArray, minValue, pop, result, D, GenN0);
		}
	}
	
	public static void sortByFitness(int[][] popTable, int[] fitArray, int[][] sortedPopMod)
	{
		int[][] tempArray = new int[popTable.length][popTable[0].length + 1];
		for(int i = 0; i < tempArray.length;i++)
		{
			for(int x = 0; x < tempArray[0].length;x++)
			{
				if(x == popTable[0].length){
					tempArray[i][x] = fitArray[i];
				}
				else
				tempArray[i][x] = popTable[i][x]; 
			}
		}
        Arrays.sort(tempArray, new Comparator<int[]>() {
          @Override             
          // Compare values according to columns
          public int compare(final int[] entry1, 
                             final int[] entry2) {
 
            if (entry1[tempArray[0].length-1] > entry2[tempArray[0].length-1])
                return 1;
            else
                return -1;
          }
        });  // End of function call sort().
        
        for(int i = 0; i < sortedPopMod.length;i++)
		{
			for(int x = 0; x < sortedPopMod[0].length;x++)
			{
					sortedPopMod[i][x] = tempArray[i][x]; 
			}
		}
	}
	
	public static void divideArray(int[][] sortedPopMod,int[][] newPopMod)
	{	
		int elements = sortedPopMod.length;
		int mod = elements % 3;
		int div = elements / 3;

		int sizeLeft = div + (mod > 0 ? 1 : 0);
		int sizeCenter = div;
		int sizeRight = div + (mod > 1 ? 1 : 0);
		int temp;
		
		if(sizeLeft > sizeCenter && sizeLeft > sizeRight)
		{
			temp = sizeLeft;
			sizeLeft = sizeCenter;
			sizeCenter = temp;
		}	
		int goTo = sizeLeft + sizeCenter;
		for(int i = 0; i < goTo;i++)
		{
			for(int x = 0; x < sortedPopMod[0].length;x++)
			{
				newPopMod[i][x] = sortedPopMod[i][x]; 
			}
		}
		int count = 0;
		for(int i = goTo; i < sortedPopMod.length;i++)//something here
		{
			for(int x = 0; x < sortedPopMod[0].length;x++)
			{
				newPopMod[i][x] = sortedPopMod[count][x]; 
			}
			count++;
		}	
	}
	
	public static void goCMR(int[][] newPopMod, int[][] geneticMod,int Cr,int Mu)
	{
		int min = 0;
		int max = geneticMod.length-1;
		int indexCount = 0;
		int[] usedIndexs = new int [geneticMod.length];
		for(int z = 0;z<geneticMod.length;z++)
			usedIndexs[z]=0;
		boolean picked = true;
		boolean canPickXOver = true;
		Random random = new Random();
		int pick, rand = 0, valOne=0, valTwo=0, set = 0;
		while(indexCount < geneticMod.length)//LOOP
		{
			pick = random.nextInt(100 - 0 + 1) + 0;
			if(pick <= Cr && canPickXOver == true)
			{
				while(picked == true && set < 2)
				{
					rand  = random.nextInt(max - min + 1) + min;
					picked = indexUsed(usedIndexs, rand);
					if(picked == false)
					{
						usedIndexs[rand] = 1;
						if(set==0)
							valOne = rand;
						else if(set == 1)
							valTwo = rand;
						set++;
						picked = true;
					}
					
				}
				picked = true;
				set = 0;
				crossOver(geneticMod, newPopMod, valOne, valTwo, indexCount);
				indexCount += 2;
			}
			else if(pick > Cr && pick <= Cr + Mu)
			{
				while(picked == true)
				{
					rand  = random.nextInt(max - min + 1) + min;
					picked = indexUsed(usedIndexs, rand);
					if(picked == false)
					{
						usedIndexs[rand] = 1;
					}
				}
				picked = true;
				mutation(geneticMod, newPopMod, rand, indexCount);
				indexCount++;
			}
			else if(pick > Cr + Mu)
			{
				while(picked == true)
				{
					rand  = random.nextInt(max - min + 1) + min;
					picked = indexUsed(usedIndexs, rand);
					if(picked == false)
					{
						usedIndexs[rand] = 1;
					}
				}
				picked = true;
				for(int i = 0;i<geneticMod[0].length;i++)
				{
					geneticMod[indexCount][i] = newPopMod[rand][i];
				}
				indexCount++;
			}
			
			if(max-indexCount < 2)
			{
				canPickXOver = false;
			}
		}
	}
	
	public static boolean indexUsed(int[] usedIndexs, int rand)
	{	
		if(usedIndexs[rand] != 0 ){
			return true;
		}
		else{
			return false;
		}
	}
	
	public static void crossOver(int[][]geneticMod, int[][]newPopMod, int valOne, int valTwo, int indexCount)
	{
		int min = 2, max = geneticMod[0].length - 2, val=0, dupCount=0;
		Random random = new Random();
		int xOver  = random.nextInt(max - min + 1) + min;
		int[][] tempArray = new int[2][geneticMod[0].length];
		int[][] duplicates = new int[2][geneticMod[0].length];
		for(int x = 0; x < tempArray.length; x++)
		{
			for(int y = 0; y < xOver; y++)
			{
				if(x==0)
					val = valOne;
				else if(x==1)
					val = valTwo;
				tempArray[x][y] = newPopMod[val][y];
			}
			for(int z = xOver; z < tempArray[0].length; z++)
			{
				if(x==0)
					val = valTwo;
				else if(x==1)
					val = valOne;
				tempArray[x][z] = newPopMod[val][z];
			}
		}
		//TEMP ARRAY DONE
		for(int z = 0;z < 2;z++)
		{
			for(int x = 0;x<tempArray[0].length;x++)
			{
				for(int y = x + 1;y<tempArray[0].length;y++)
				{
					if (x!=y && tempArray[z][x] == tempArray[z][y])
					{
						duplicates[z][y] = tempArray[z][y];
						dupCount++;
					}
				}
			}
		}	
		//DUP ARRAY DONE
		int dupTempOne=0,dupTempTwo=0,temp=0;
		boolean dupOneFound = false, dupTwoFound = false;
		for(int x = 0;x<dupCount;x++)
		{
			int y = 0;
			int z = duplicates[0].length-1;
			while(y<duplicates[0].length && dupOneFound==false)
			{
				if(duplicates[0][y] != 0)
				{
					dupTempOne = duplicates[0][y];
					duplicates[0][y] = 0;
					dupOneFound = true;
				}
				y++;
			}
			while(z >=0 && dupTwoFound == false)
			{
				if(duplicates[1][z] != 0)
				{
					dupTempTwo = duplicates[1][z];
					duplicates[1][z] = 0;
					dupTwoFound = true;
				}
				z--;
			}
			temp = dupTempOne;
			dupTempOne = dupTempTwo;
			dupTempTwo = temp;
			tempArray[0][y-1] = dupTempOne;
			tempArray[1][z+1] = dupTempTwo;
			dupOneFound = false;
			dupTwoFound = false;
			x++;
		}
		for(int a = 0;a<tempArray.length;a++)
		{
			for(int b = 0; b < tempArray[0].length;b++)
			{
				geneticMod[(indexCount + a)][b] = tempArray[a][b];
			}
		}
		
	}
	
	public static void mutation(int[][] geneticMod, int[][] newPopMod, int index, int indexCount)
	{
		int min = 0, max = geneticMod[0].length-1; 
		int indexOne = 0, indexTwo=0, rand, count = 0;
		Random random = new Random();
		while(count < 2)
		{
			rand  = random.nextInt(max - min + 1) + min;
			if(count == 0){
				indexOne = rand;
				count++;
			}
			else if(count == 1 && indexOne != rand){
				indexTwo = rand;
				count++;
			}
		}
		for(int x = 0; x<geneticMod[0].length;x++)
		{
			if(indexOne == x ){
				geneticMod[indexCount][x] = newPopMod[index][indexTwo];
			}
			else if(indexTwo == x){
				geneticMod[indexCount][x] = newPopMod[index][indexOne];
			}
			else
				geneticMod[indexCount][x] = newPopMod[index][x];
		}
	}
	
	public static void printGen(int[] fitnessArray,int minValue,int[][] geneticMod,int examsPerDay,int session, int GenN0)
	{
		System.out.println("Generation "+(GenN0+1));
		System.out.print("Ordering: ");
		for(int x = 0;x<geneticMod[0].length;x++)
		{
			System.out.print(geneticMod[minValue][x]+" ");
		}
		System.out.println("\n");
		String fmt = "%13s";
		String fmtI = "%-22d";
		for(int z = 0;z<session;z++)
		{
			System.out.print("Session "+(z+1));
			System.out.format(fmt,"");
		}
		System.out.println();
		for(int a = 0;a<examsPerDay;a++)
		{
			for(int b = a;b<geneticMod[0].length;b+=examsPerDay)
			{
				System.out.format(fmtI, geneticMod[minValue][b]);
			}
			System.out.println();
		}
		System.out.println();
		System.out.println("Fitness Cost: "+fitnessArray[minValue]);
		System.out.println("-------------------------------------------------------------------------");
	}
}
