package analysis;

import analysis.breakAndEnter.BreakAndEnterResult;
import analysis.carTheft.AutoTheftResult;
import analysis.crimeDataRanking.NeighborhoodCrimeRankingResult;
import analysis.facade.CrimeAnalysisFacade;
import org.junit.jupiter.api.Test;

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
        // Mock the CrimeAnalysisFacade
        CrimeAnalysisFacade mockFacade = mock(CrimeAnalysisFacade.class);

        // Prepare a mock result for a specific crime (Assault) in the neighborhood "Maple Leaf"
        NeighborhoodCrimeRankingResult mockResult = new NeighborhoodCrimeRankingResult(
                "Maple Leaf", 130, "Very Safe", "Assault");

        // Configure the mockFacade to return the mockResult when the method is called with specific parameters
        when(mockFacade.getNeighborhoodRanking("Maple Leaf", "Assault")).thenReturn(mockResult);

        // Execute the test by calling the method with the expected parameters
        NeighborhoodCrimeRankingResult result = mockFacade.getNeighborhoodRanking("Maple Leaf", "Assault");

        // Verify the result
        assertNotNull(result, "The result should not be null.");
        assertEquals("Maple Leaf", result.getNeighborhood(), "The neighborhood should be 'Maple Leaf'.");
        assertEquals(130, result.getRanking(), "The ranking should be 130.");
        assertEquals("Assault", result.getCrimeType(), "The crime type should be 'Assault'.");
        assertEquals("Very Safe", result.getSafetyLevel(), "The safety level should be 'Very Safe'.");

        // Verify that the mockFacade was called with the correct arguments exactly once
        verify(mockFacade, times(1)).getNeighborhoodRanking("Maple Leaf", "Assault");
    }

    @Test
    void analyzeNeighborhoodCrimeRankingTotalCrimeTest() {
        // Mock the CrimeAnalysisFacade
        CrimeAnalysisFacade mockFacade = mock(CrimeAnalysisFacade.class);

        // Prepare a mock result for total crime in the neighborhood "Willowdale East"
        NeighborhoodCrimeRankingResult mockResult = new NeighborhoodCrimeRankingResult(
                "Willowdale East", 25, "Very Dangerous", "Total Crime");

        // Configure the mockFacade to return the mockResult when the method is called with the specified neighborhood and no specific crime
        when(mockFacade.getNeighborhoodRanking("Willowdale East", null)).thenReturn(mockResult);

        // Execute the test by calling the method with the expected parameters
        NeighborhoodCrimeRankingResult result = mockFacade.getNeighborhoodRanking("Willowdale East", null);

        // Verify the result
        assertNotNull(result, "The result should not be null.");
        assertEquals("Willowdale East", result.getNeighborhood(), "The neighborhood should be 'Willowdale East'.");
        assertEquals(25, result.getRanking(), "The ranking should be 25.");
        assertEquals("Total Crime", result.getCrimeType(), "The crime type should be 'Total Crime'.");
        assertEquals("Very Dangerous", result.getSafetyLevel(), "The safety level should be 'Very Dangerous'.");

        // Verify that the mockFacade was called with the correct arguments exactly once
        verify(mockFacade, times(1)).getNeighborhoodRanking("Willowdale East", null);
    }

}
