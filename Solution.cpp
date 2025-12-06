
#include <span>
#include <queue>
#include <vector>
#include <algorithm>
using namespace std;

class Solution {

    inline static const int MASTER_SERVER = 0;
    int numberOfServers = 0;

public:
    int networkBecomesIdle(const vector<vector<int>>& edges, vector<int>& patience) {
        numberOfServers = patience.size();
        vector<vector<int>> undirectedGraph = createUndirectedGraph(edges);
        return findMinTimeWhenTheNetworkBecomesIdle(undirectedGraph, patience);
    }

private:
    int findMinTimeWhenTheNetworkBecomesIdle(span<const vector<int>> undirectedGraph, span<int> patience) const {
        queue<int> queue;
        vector<bool> visited(numberOfServers);

        queue.push(MASTER_SERVER);
        visited[MASTER_SERVER] = true;

        int distanceFromMasterServer = 0;
        int minTimeWhenTheNetworkBecomesIdle = 0;

        while (!queue.empty()) {
            int numberOfServersInCurrentRound = queue.size();

            while (numberOfServersInCurrentRound > 0) {
                int server = queue.front();
                queue.pop();

                int time = calculateTimeForServerToBecomeIdle(distanceFromMasterServer, patience[server]);
                minTimeWhenTheNetworkBecomesIdle = max(minTimeWhenTheNetworkBecomesIdle, time);

                for (int nextServer : undirectedGraph[server]) {
                    if (visited[nextServer]) {
                        continue;
                    }
                    visited[nextServer] = true;
                    queue.push(nextServer);
                }
                --numberOfServersInCurrentRound;
            }
            ++distanceFromMasterServer;
        }

        return minTimeWhenTheNetworkBecomesIdle + 1;
    }

    vector<vector<int>> createUndirectedGraph(span<const vector<int>> edges)  const {
        vector<vector<int>> undirectedGraph(numberOfServers);

        for (const auto& edge : edges) {
            int firstServer = edge[0];
            int secondServer = edge[1];
            undirectedGraph[firstServer].push_back(secondServer);
            undirectedGraph[secondServer].push_back(firstServer);
        }
        return undirectedGraph;
    }

    int calculateTimeForServerToBecomeIdle(int distanceFromMasterServer, int patience) const {
        int roundTripFromServerToMasterServer = 2 * distanceFromMasterServer;
        if (patience == 0) {
            return 2 * roundTripFromServerToMasterServer;
        }

        int timeBetweenFirstAndLastSignalFromServer = ((2 * distanceFromMasterServer - 1) / patience) * patience;
        return roundTripFromServerToMasterServer + timeBetweenFirstAndLastSignalFromServer;
    }
};
