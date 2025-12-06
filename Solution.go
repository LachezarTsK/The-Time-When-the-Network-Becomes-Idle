
package main

const MASTER_SERVER = 0
var numberOfServers int

func networkBecomesIdle(edges [][]int, patience []int) int {
    numberOfServers = len(patience)
    undirectedGraph := createUndirectedGraph(edges)
    return findMinTimeWhenTheNetworkBecomesIdle(undirectedGraph, patience)
}

func findMinTimeWhenTheNetworkBecomesIdle(undirectedGraph [][]int, patience []int) int {
    // Alternatively:  queue := list.List{}, imported from "container/list"
    queue := []int{}
    visited := make([]bool, numberOfServers)

    queue = append(queue, MASTER_SERVER)
    visited[MASTER_SERVER] = true

    distanceFromMasterServer := 0
    minTimeWhenTheNetworkBecomesIdle := 0

    for len(queue) > 0 {
        numberOfServersInCurrentRound := len(queue)

        for numberOfServersInCurrentRound > 0 {
            server := queue[0]
            queue = queue[1:]

            time := calculateTimeForServerToBecomeIdle(distanceFromMasterServer, patience[server])
            minTimeWhenTheNetworkBecomesIdle = max(minTimeWhenTheNetworkBecomesIdle, time)

            for _, nextServer := range undirectedGraph[server] {
                if visited[nextServer] {
                    continue
                }
                visited[nextServer] = true
                queue = append(queue, nextServer)
            }
            numberOfServersInCurrentRound--
        }
        distanceFromMasterServer++
    }

    return minTimeWhenTheNetworkBecomesIdle + 1
}

func createUndirectedGraph(edges [][]int) [][]int {
    undirectedGraph := make([][]int, numberOfServers)
    for server := range numberOfServers {
        undirectedGraph[server] = []int{}
    }

    for _, edge := range edges {
        firstServer := edge[0]
        secondServer := edge[1]
        undirectedGraph[firstServer] = append(undirectedGraph[firstServer], secondServer)
        undirectedGraph[secondServer] = append(undirectedGraph[secondServer], firstServer)
    }
    return undirectedGraph
}

func calculateTimeForServerToBecomeIdle(distanceFromMasterServer int, patience int) int {
    roundTripFromServerToMasterServer := 2 * distanceFromMasterServer
    if patience == 0 {
        return 2 * roundTripFromServerToMasterServer
    }

    timeBetweenFirstAndLastSignalFromServer := ((2 * distanceFromMasterServer - 1) / patience) * patience
    return roundTripFromServerToMasterServer + timeBetweenFirstAndLastSignalFromServer
}
