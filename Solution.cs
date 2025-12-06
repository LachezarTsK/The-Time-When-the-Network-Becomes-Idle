
using System;
using System.Collections.Generic;

public class Solution
{
    private static readonly int MASTER_SERVER = 0;
    private int numberOfServers;

    public int NetworkBecomesIdle(int[][] edges, int[] patience)
    {
        numberOfServers = patience.Length;
        List<int>[] undirectedGraph = CreateUndirectedGraph(edges);
        return FindMinTimeWhenTheNetworkBecomesIdle(undirectedGraph, patience);
    }

    private int FindMinTimeWhenTheNetworkBecomesIdle(List<int>[] undirectedGraph, int[] patience)
    {
        Queue<int> queue = new Queue<int>();
        bool[] visited = new bool[numberOfServers];

        queue.Enqueue(MASTER_SERVER);
        visited[MASTER_SERVER] = true;

        int distanceFromMasterServer = 0;
        int minTimeWhenTheNetworkBecomesIdle = 0;

        while (queue.Count > 0)
        {
            int numberOfServersInCurrentRound = queue.Count;

            while (numberOfServersInCurrentRound > 0)
            {
                int server = queue.Dequeue();

                int time = CalculateTimeForServerToBecomeIdle(distanceFromMasterServer, patience[server]);
                minTimeWhenTheNetworkBecomesIdle = Math.Max(minTimeWhenTheNetworkBecomesIdle, time);

                foreach (int nextServer in undirectedGraph[server])
                {
                    if (visited[nextServer])
                    {
                        continue;
                    }
                    visited[nextServer] = true;
                    queue.Enqueue(nextServer);
                }
                --numberOfServersInCurrentRound;
            }
            ++distanceFromMasterServer;
        }

        return minTimeWhenTheNetworkBecomesIdle + 1;
    }

    private List<int>[] CreateUndirectedGraph(int[][] edges)
    {
        List<int>[] undirectedGraph = new List<int>[numberOfServers];
        for (int server = 0; server < numberOfServers; ++server)
        {
            undirectedGraph[server] = new List<int>();
        }

        foreach (int[] edge in edges)
        {
            int firstServer = edge[0];
            int secondServer = edge[1];
            undirectedGraph[firstServer].Add(secondServer);
            undirectedGraph[secondServer].Add(firstServer);
        }
        return undirectedGraph;
    }

    private int CalculateTimeForServerToBecomeIdle(int distanceFromMasterServer, int patience)
    {
        int roundTripFromServerToMasterServer = 2 * distanceFromMasterServer;
        if (patience == 0)
        {
            return 2 * roundTripFromServerToMasterServer;
        }

        int timeBetweenFirstAndLastSignalFromServer = ((2 * distanceFromMasterServer - 1) / patience) * patience;
        return roundTripFromServerToMasterServer + timeBetweenFirstAndLastSignalFromServer;
    }
}
