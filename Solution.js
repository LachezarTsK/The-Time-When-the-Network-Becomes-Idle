
// const {Queue} = require('@datastructures-js/queue');
/*
 Queue is internally included in the solution file on leetcode.
 When running the code on leetcode, the line above should stay commented out. 
 It is mentioned here just for information about the external library 
 that is applied for this data structure.
 */

/**
 * @param {number[][]} edges
 * @param {number[]} patience
 * @return {number}
 */
var networkBecomesIdle = function (edges, patience) {
    util.numberOfServers = patience.length;
    const undirectedGraph = createUndirectedGraph(edges);
    return findMinTimeWhenTheNetworkBecomesIdle(undirectedGraph, patience);
};

const util = {MASTER_SERVER: 0, numberOfServers: 0};

/**
 * @param {number[][]} undirectedGraph
 * @param {number[]} patience
 * @return {number}
 */
function findMinTimeWhenTheNetworkBecomesIdle(undirectedGraph, patience) {
    const queue = new Queue();
    const visited = new Array(util.numberOfServers).fill(false);

    queue.enqueue(util.MASTER_SERVER);
    visited[util.MASTER_SERVER] = true;

    let distanceFromMasterServer = 0;
    let minTimeWhenTheNetworkBecomesIdle = 0;

    while (!queue.isEmpty()) {
        let numberOfServersInCurrentRound = queue.size();

        while (numberOfServersInCurrentRound > 0) {
            const server = queue.dequeue();

            const time = calculateTimeForServerToBecomeIdle(distanceFromMasterServer, patience[server]);
            minTimeWhenTheNetworkBecomesIdle = Math.max(minTimeWhenTheNetworkBecomesIdle, time);

            for (let nextServer of undirectedGraph[server]) {
                if (visited[nextServer]) {
                    continue;
                }
                visited[nextServer] = true;
                queue.enqueue(nextServer);
            }
            --numberOfServersInCurrentRound;
        }
        ++distanceFromMasterServer;
    }

    return minTimeWhenTheNetworkBecomesIdle + 1;
}

/**
 * @param {number[][]} edges
 * @return {number[][]}
 */
function createUndirectedGraph(edges) {
    const undirectedGraph = Array.from(new Array(util.numberOfServers), () => new Array());
    for (let server = 0; server < util.numberOfServers; ++server) {
        undirectedGraph[server] = new Array();
    }

    for (let[firstServer, secondServer] of edges) {
        undirectedGraph[firstServer].push(secondServer);
        undirectedGraph[secondServer].push(firstServer);
    }
    return undirectedGraph;
}

/**
 * @param {number} distanceFromMasterServer
 * @param {number} patience
 * @return {number}
 */
function calculateTimeForServerToBecomeIdle(distanceFromMasterServer, patience) {
    const roundTripFromServerToMasterServer = 2 * distanceFromMasterServer;
    if (patience === 0) {
        return 2 * roundTripFromServerToMasterServer;
    }

    const timeBetweenFirstAndLastSignalFromServer = Math.floor((2 * distanceFromMasterServer - 1) / patience) * patience;
    return roundTripFromServerToMasterServer + timeBetweenFirstAndLastSignalFromServer;
}
