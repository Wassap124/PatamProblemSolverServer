package Matrices;

import Algorithms.GoalSearchable;
import Algorithms.Searchable;
import Algorithms.State;

import java.util.*;
import java.util.stream.Collectors;


public class Matrix implements GoalSearchable<Index>
{
    private int rows;
    private int columns;
    private Double[][] matrix;
    private Index enteryIndex;
    private Index exitIndex;


    public Matrix()
    {
        this(new Double[0][0],null,null);
    }

    public Matrix(Double[][] matrix,Index enteryIndex,Index exitIndex)
    {
        this(matrix.length,
                (matrix.length>0)?matrix[0].length:0,
                matrix,enteryIndex,exitIndex);
    }

    private Matrix(int rows, int columns, Double[][] matrix,Index enteryIndex,Index exitIndex)
    {
        this.rows = rows;
        this.columns = columns;
        this.matrix = matrix;
        this.enteryIndex=enteryIndex;
        this.exitIndex=exitIndex;
    }

    @Override
    public State<Index> getInitialState()
    {
        double cost=0;
        try
        {
            cost=this.matrix[enteryIndex.row][enteryIndex.column];
        }catch (Exception ignore){}
        return new State<>(enteryIndex,cost,null);
    }

    @Override
    public boolean isGoalState(State<Index> state)
    {
        return(exitIndex.equals(state.getState()));
    }

    @Override
    public Collection<State<Index>> getAllPossibleStates(State<Index> s)
    {
        List<State<Index>> states= new ArrayList<>();
        Index index=s.getState();
        int currentRow=index.row;
        int currentColumn=index.column;
        if(currentRow-1 >= 0)
            states.add(new State<>(new Index(currentRow-1,currentColumn),s.getCost()+this.matrix[currentRow-1][currentColumn],s));
        if(currentRow+1 < rows)
            states.add(new State<>(new Index(currentRow+1,currentColumn),s.getCost()+this.matrix[currentRow+1][currentColumn],s));
        if(currentColumn-1 >= 0)
            states.add(new State<>(new Index(currentRow,currentColumn-1),s.getCost()+this.matrix[currentRow][currentColumn-1],s));
        if(currentColumn+1 < columns)
            states.add(new State<>(new Index(currentRow,currentColumn+1),s.getCost()+this.matrix[currentRow][currentColumn+1],s));

        states = states.stream().filter(indexState -> indexState.getCost()<Double.MAX_VALUE).collect(Collectors.toList());
        Collections.shuffle(states);
        return states;
    }

    @Override
    public State<Index> getGoalState() {
        return new State<>(exitIndex,0,null);
    }

    public String getHashOfMatrix() {
    	String matStr = "s_" + this.enteryIndex.row + "," + this.enteryIndex.column +
    			"e_" + this.exitIndex.row + "," + this.exitIndex.column + "v_";
    	for(int i = 0; i < this.matrix.length; i++) {
    		for(int j = 0; j < this.matrix[0].length; j++) {
    			matStr = matStr + this.matrix[i][j];
    		}
    	}
    	return matStr.hashCode() + "";
    }
}
