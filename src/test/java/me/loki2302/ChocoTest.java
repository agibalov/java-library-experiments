package me.loki2302;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.IntVar;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ChocoTest {
    @Test
    public void canGetAllSolutions() {
        Model model = new Model();
        IntVar x = model.intVar(0, 5);
        IntVar y = model.intVar(0, 5);
        x.add(y).le(5).post();

        List<Solution> solutions = model.getSolver().findAllSolutions();
        assertEquals(21, solutions.size());
        for(Solution solution : solutions) {
            int xValue = solution.getIntVal(x);
            int yValue = solution.getIntVal(y);
            assertTrue(xValue + yValue <= 5);
        }
    }

    @Test
    public void canGetOptimalSolutions() {
        Model model = new Model();
        IntVar x = model.intVar(0, 5);
        IntVar y = model.intVar(0, 5);
        x.add(y).le(5).post();

        List<Solution> solutions = model.getSolver().findAllOptimalSolutions(x, true);
        assertEquals(1, solutions.size());
        for(Solution solution : solutions) {
            int xValue = solution.getIntVal(x);
            int yValue = solution.getIntVal(y);
            assertTrue(xValue + yValue <= 5);
        }
    }
}
