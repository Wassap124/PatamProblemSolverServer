package server_side;

import Algorithms.*;
import Matrices.Direction;
import Matrices.Index;
import Matrices.Matrix;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class MyClientHandler implements ClientHandler
{

    private CacheManager<String,String> CacheManager;

    public MyClientHandler(CacheManager<String,String> CacheManager)
    {
        this.CacheManager=CacheManager;
    }

    @Override
    public void handleClient(InputStream inFromClient, OutputStream outToClient)
    {
        try
        {
            BufferedReader userInput = new BufferedReader(new InputStreamReader(inFromClient));
            PrintWriter outToScreen = new PrintWriter(outToClient);
            StringWriter stringWriter = new StringWriter();
            PrintWriter outToServer = new PrintWriter(stringWriter);
            readInputsAndSend(userInput, outToServer, "end");

            String stringClientInput = stringWriter.getBuffer().toString();
            String[] split = stringClientInput.split(System.lineSeparator());
            Double[][] matrixInts = new Double[split.length][split[0].split(",").length];
            for (int i = 0; i < matrixInts.length; i++) {
                String[] split1 = split[i].split(",");
                for (int j = 0; j < split1.length; j++) {
                    matrixInts[i][j] = Double.parseDouble(split1[j]);
                }
            }
            String enteryLine = userInput.readLine();
            String exitLine = userInput.readLine();
            Index enteryIndex = new Index(enteryLine);
            Index exitIndex = new Index(exitLine);


            Matrix matrix = new Matrix(matrixInts, enteryIndex, exitIndex);
            String solution=null;
            boolean flag = false;
            String matHash = matrix.getHashOfMatrix();
            if(CacheManager.isSolutionExist(matHash))
            {
                try {
                    solution = CacheManager.getSolution(matHash);
                    flag=true;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    flag=false;
                }
            }
            if(!flag)
            {
                Searcher<Index, Collection<Direction>> indexIndexSearcher = new AStar<>((goalState, initialState) ->
                {
                    ArrayList<Direction> directions = new ArrayList<>();
                    State<Index> current = goalState;
                    do {
                        if (current.getCameFrom() != null) {
                            Index parent = current.getCameFrom().getState();
                            Direction direction = parent.directionToWhere(current.getState());
                            directions.add(0, direction);
                        }
                    } while ((current = current.getCameFrom()) != null);
                    return directions;
                });
                Collection<Direction> search = indexIndexSearcher.search(matrix);
                ArrayList<Direction> directions = new ArrayList<>(search!=null?search:new ArrayList<>());

                StringBuilder stringBuilder = new StringBuilder();

                for (int i = 0; i < directions.size(); i++) {
                    stringBuilder.append(directions.get(i));
                    if (i < directions.size() - 1)
                        stringBuilder.append(",");
                }
                solution= stringBuilder.toString();
                CacheManager.saveSolution(matHash, solution);
            }else {

            }

            outToScreen.println(solution);
            outToScreen.flush();
        }catch (IOException  e)
        {
            e.printStackTrace();
        }


    }
    private void readInputsAndSend(BufferedReader in, PrintWriter out, String exitStr)
    {
        try
        {
            String line;
            while(!(line=in.readLine()).equals(exitStr))
            {
                out.println(line);
                out.flush();
            }
        } catch (IOException e) { e.printStackTrace();}
    }
}
