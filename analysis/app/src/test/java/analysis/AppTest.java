package analysis;

import analysis.breakAndEnter.BreakAndEnterResult;
import analysis.carTheft.AutoTheftResult;
import analysis.crimeDataRanking.NeighborhoodCrimeRankingResult;
import analysis.facade.CrimeAnalysisFacade;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppTest {

    @Test
    void analyzeBreakAndEnterTest() {
        // Mock CrimeAnalysisFacade
        CrimeAnalysisFacade mockFacade = mock(CrimeAnalysisFacade.class);

        // Mock the result to return the expected data
        BreakAndEnterResult mockResult = mock(BreakAndEnterResult.class);
        when(mockResult.getPastYearIncidents()).thenReturn(List.of(
                new BreakAndEnterResult.Incident("2023-August-18", 0.00),
                new BreakAndEnterResult.Incident("2023-August-19", 194.39)
        ));
        when(mockResult.getAllKnownIncidents()).thenReturn(List.of(
                new BreakAndEnterResult.Incident("2016-February-24", 0.00),
                new BreakAndEnterResult.Incident("2016-July-30", 156.12),
                new BreakAndEnterResult.Incident("2020-December-24", 0.00),
                new BreakAndEnterResult.Incident("2020-November-25", 0.00),
                new BreakAndEnterResult.Incident("2021-August-24", 0.00),
                new BreakAndEnterResult.Incident("2023-August-18", 0.00),
                new BreakAndEnterResult.Incident("2023-August-19", 194.39),
                new BreakAndEnterResult.Incident("2023-January-17", 171.40),
                new BreakAndEnterResult.Incident("2023-June-2", 170.20)
        ));
        when(mockResult.getWarning()).thenReturn("Don't live here!");

        // Set up the mockFacade to return the mockResult
        when(mockFacade.analyzeBreakAndEnter(anyDouble(), anyDouble(), anyInt(), anyInt())).thenReturn(mockResult);

        // Execute the test
        BreakAndEnterResult result = mockFacade.analyzeBreakAndEnter(43.8062893908425, -79.1803868891903, 200, 3);

        // Verify the result
        assertNotNull(result);
        assertEquals(2, result.getPastYearIncidents().size(), "Should return 2 incidents for the past year within the radius");
        assertEquals(9, result.getAllKnownIncidents().size(), "Should return 9 known incidents within the radius");
        assertEquals("Don't live here!", result.getWarning());

        // Verify that the mock was called with the expected arguments
        verify(mockFacade, times(1)).analyzeBreakAndEnter(43.8062893908425, -79.1803868891903, 200, 3);
    }

    @Test
    void analyzeAutoTheftTest() {
        // Mock CrimeAnalysisFacade
        CrimeAnalysisFacade mockFacade = mock(CrimeAnalysisFacade.class);

        // Mock the result to return the expected data
        AutoTheftResult mockResult = mock(AutoTheftResult.class);
        when(mockResult.getPastYearIncidents()).thenReturn(List.of(
                new AutoTheftResult.Incident("2023-December-22", 69.56)
        ));
        when(mockResult.getAllKnownIncidents()).thenReturn(List.of(
                new AutoTheftResult.Incident("2019-September-10", 145.62),
                new AutoTheftResult.Incident("2020-June-26", 69.56),
                new AutoTheftResult.Incident("2021-April-12", 69.56),
                new AutoTheftResult.Incident("2023-April-7", 69.56),
                new AutoTheftResult.Incident("2023-December-22", 69.56),
                new AutoTheftResult.Incident("2023-June-2", 69.56),
                new AutoTheftResult.Incident("2023-June-2", 69.56)
        ));
        when(mockResult.getWarning()).thenReturn("Safe to park here!");

        // Set up the mockFacade to return the mockResult
        when(mockFacade.analyzeAutoTheft(anyDouble(), anyDouble(), anyInt(), anyInt(), anyInt())).thenReturn(mockResult);

        // Execute the test
        AutoTheftResult result = mockFacade.analyzeAutoTheft(43.8062893908425, -79.1803868891903, 200, 5, 2018);

        // Verify the result
        assertNotNull(result);
        assertEquals(1, result.getPastYearIncidents().size(), "Should return 1 incident for the past year within the radius");
        assertEquals(7, result.getAllKnownIncidents().size(), "Should return 7 known incidents within the radius");
        assertEquals("Safe to park here!", result.getWarning());

        // Verify that the mock was called with the expected arguments
        verify(mockFacade, times(1)).analyzeAutoTheft(43.8062893908425, -79.1803868891903, 200, 5, 2018);
    }

    @Test
    void analyzeNeighborhoodCrimeRankingSpecificCrimeTest() {
        // Mock CrimeAnalysisFacade
        CrimeAnalysisFacade mockFacade = mock(CrimeAnalysisFacade.class);

        // Mock the result to return the expected data
        NeighborhoodCrimeRankingResult mockResult = mock(NeighborhoodCrimeRankingResult.class);
        when(mockResult.getNeighborhood()).thenReturn("Maple Leaf (29)");
        when(mockResult.getRanking()).thenReturn(130);
        when(mockResult.getCrimeType()).thenReturn("Assault");
        when(mockResult.getSafetyLevel()).thenReturn("Very Safe");

        // Set up the mockFacade to return the mockResult
        when(mockFacade.getNeighborhoodRanking(eq("Maple Leaf (29)"), eq("Assault"))).thenReturn(mockResult);

        // Execute the test
        NeighborhoodCrimeRankingResult result = mockFacade.getNeighborhoodRanking("Maple Leaf (29)", "Assault");

        // Verify the result
        assertNotNull(result);
        assertEquals("Maple Leaf (29)", result.getNeighborhood());
        assertEquals(130, result.getRanking());
        assertEquals("Assault", result.getCrimeType());
        assertEquals("Very Safe", result.getSafetyLevel());

        // Verify that the mock was called with the expected arguments
        verify(mockFacade, times(1)).getNeighborhoodRanking("Maple Leaf (29)", "Assault");
    }

    @Test
    void analyzeNeighborhoodCrimeRankingTotalCrimeTest() {
        // Mock CrimeAnalysisFacade
        CrimeAnalysisFacade mockFacade = mock(CrimeAnalysisFacade.class);

        // Mock the result to return the expected data
        NeighborhoodCrimeRankingResult mockResult = mock(NeighborhoodCrimeRankingResult.class);
        when(mockResult.getNeighborhood()).thenReturn("Willowdale East (51)");
        when(mockResult.getRanking()).thenReturn(25);
        when(mockResult.getCrimeType()).thenReturn("Total Crime");
        when(mockResult.getSafetyLevel()).thenReturn("Very Dangerous");

        // Set up the mockFacade to return the mockResult
        when(mockFacade.getNeighborhoodRanking(eq("Willowdale East (51)"), eq(""))).thenReturn(mockResult);

        // Execute the test
        NeighborhoodCrimeRankingResult result = mockFacade.getNeighborhoodRanking("Willowdale East (51)", "");

        // Verify the result
        assertNotNull(result);
        assertEquals("Willowdale East (51)", result.getNeighborhood());
        assertEquals(25, result.getRanking());
        assertEquals("Total Crime", result.getCrimeType());
        assertEquals("Very Dangerous", result.getSafetyLevel());

        // Verify that the mock was called with the expected arguments
        verify(mockFacade, times(1)).getNeighborhoodRanking("Willowdale East (51)", "");
    }
}
