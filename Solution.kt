
import kotlin.math.max

class Solution {

    private companion object {
        const val MASTER_SERVER = 0
    }

    private var numberOfServers = 0

    fun networkBecomesIdle(edges: Array<IntArray>, patience: IntArray): Int {
        numberOfServers = patience.size
        val undirectedGraph = createUndirectedGraph(edges)
        return findMinTimeWhenTheNetworkBecomesIdle(undirectedGraph, patience)
    }

    private fun findMinTimeWhenTheNetworkBecomesIdle(undirectedGraph: Array<MutableList<Int>>, patience: IntArray): Int {
        val queue = mutableListOf<Int>()
        val visited = Array<Boolean>(numberOfServers) { false }

        queue.add(MASTER_SERVER)
        visited[MASTER_SERVER] = true

        var distanceFromMasterServer = 0
        var minTimeWhenTheNetworkBecomesIdle = 0

        while (!queue.isEmpty()) {
            var numberOfServersInCurrentRound = queue.size

            while (numberOfServersInCurrentRound > 0) {
                val server = queue.removeFirst()

                val time = calculateTimeForServerToBecomeIdle(distanceFromMasterServer, patience[server])
                minTimeWhenTheNetworkBecomesIdle = max(minTimeWhenTheNetworkBecomesIdle, time)

                for (nextServer in undirectedGraph[server]) {
                    if (visited[nextServer]) {
                        continue
                    }
                    visited[nextServer] = true
                    queue.add(nextServer)
                }
                --numberOfServersInCurrentRound
            }
            ++distanceFromMasterServer
        }

        return minTimeWhenTheNetworkBecomesIdle + 1
    }

    private fun createUndirectedGraph(edges: Array<IntArray>): Array<MutableList<Int>> {
        val undirectedGraph = Array<MutableList<Int>>(numberOfServers) { MutableList<Int>(0) { 0 } }

        for ((firstServer, secondServer) in edges) {
            undirectedGraph[firstServer].add(secondServer)
            undirectedGraph[secondServer].add(firstServer)
        }
        return undirectedGraph
    }

    private fun calculateTimeForServerToBecomeIdle(distanceFromMasterServer: Int, patience: Int): Int {
        val roundTripFromServerToMasterServer = 2 * distanceFromMasterServer
        if (patience == 0) {
            return 2 * roundTripFromServerToMasterServer
        }

        val timeBetweenFirstAndLastSignalFromServer = ((2 * distanceFromMasterServer - 1) / patience) * patience
        return roundTripFromServerToMasterServer + timeBetweenFirstAndLastSignalFromServer
    }
}
