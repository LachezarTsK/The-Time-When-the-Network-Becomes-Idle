
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Solution {

    private static final int MASTER_SERVER = 0;
    private int numberOfServers;

    public int networkBecomesIdle(int[][] edges, int[] patience) {
        numberOfServers = patience.length;
        List<Integer>[] undirectedGraph = createUndirectedGraph(edges);
        return findMinTimeWhenTheNetworkBecomesIdle(undirectedGraph, patience);
    }

    private int findMinTimeWhenTheNetworkBecomesIdle(List<Integer>[] undirectedGraph, int[] patience) {
        Queue<Integer> queue = new LinkedList<>();
        boolean[] visited = new boolean[numberOfServers];

        queue.add(MASTER_SERVER);
        visited[MASTER_SERVER] = true;

        int distanceFromMasterServer = 0;
        int minTimeWhenTheNetworkBecomesIdle = 0;

        while (!queue.isEmpty()) {
            int numberOfServersInCurrentRound = queue.size();

            while (numberOfServersInCurrentRound > 0) {
                int server = queue.poll();

                int time = calculateTimeForServerToBecomeIdle(distanceFromMasterServer, patience[server]);
                minTimeWhenTheNetworkBecomesIdle = Math.max(minTimeWhenTheNetworkBecomesIdle, time);

                for (int nextServer : undirectedGraph[server]) {
                    if (visited[nextServer]) {
                        continue;
                    }
                    visited[nextServer] = true;
                    queue.add(nextServer);
                }
                --numberOfServersInCurrentRound;
            }
            ++distanceFromMasterServer;
        }

        return minTimeWhenTheNetworkBecomesIdle + 1;
    }

    private List<Integer>[] createUndirectedGraph(int[][] edges) {
        List<Integer>[] undirectedGraph = new List[numberOfServers];
        for (int server = 0; server < numberOfServers; ++server) {
            undirectedGraph[server] = new ArrayList<>();
        }

        for (int[] edge : edges) {
            int firstServer = edge[0];
            int secondServer = edge[1];
            undirectedGraph[firstServer].add(secondServer);
            undirectedGraph[secondServer].add(firstServer);
        }
        return undirectedGraph;
    }

    private int calculateTimeForServerToBecomeIdle(int distanceFromMasterServer, int patience) {
        int roundTripFromServerToMasterServer = 2 * distanceFromMasterServer;
        if (patience == 0) {
            return 2 * roundTripFromServerToMasterServer;
        }

        int timeBetweenFirstAndLastSignalFromServer = ((2 * distanceFromMasterServer - 1) / patience) * patience;
        return roundTripFromServerToMasterServer + timeBetweenFirstAndLastSignalFromServer;
    }
}
