package tasks

import contributors.*
import kotlinx.coroutines.*

suspend fun loadContributorsSuspend(service: GitHubService, req: RequestData): List<User> = coroutineScope {
    val repos = service
        .getOrgRepos(req.org)
        .also { logRepos(req, it) }
        .body() ?: listOf()
        repos.map { repo ->
            async(Dispatchers.Default) {
            //async {
                log("starting loading for ${repo.name}")
                service.getRepoContributors(req.org, repo.name)
                    .also { logUsers(repo, it) }
                    .bodyList()
            }
        }.awaitAll().flatMap { it }.aggregate()
}