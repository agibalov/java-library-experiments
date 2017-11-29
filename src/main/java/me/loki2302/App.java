package me.loki2302;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.IntVar;

import java.util.List;

public class App {
    public static void main(String[] args) {
        Model model = new Model();
        IntVar x = model.intVar(0, 5);
        IntVar y = model.intVar(0, 5);
        x.add(y).le(5).post();
        x.ne(4).post();
        y.ne(2).post();

        List<Solution> solutions = model.getSolver().findAllOptimalSolutions(x, true); //model.getSolver().findAllSolutions();
        for(Solution solution : solutions) {
            System.out.printf("x=%d, y=%d\n", solution.getIntVal(x), solution.getIntVal(y));
        }
    }
}
