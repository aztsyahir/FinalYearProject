document.getElementById('input-box').addEventListener('input', function () {
    const query = this.value;
    const edid = document.getElementById('edid').value;
    console.log('EDID:', edid);
    if (query.length >= 1) {
        fetch('/SearchMember?query=' + encodeURIComponent(query) + '&edid=' + edid)
            .then(response => response.json())
            .then(data => {
                const resultsContainer = document.getElementById('member-results');
                resultsContainer.innerHTML = '';
                data.forEach(player => {
                    const playerItem = document.createElement('div');
                    playerItem.className = 'list-group-item d-flex justify-content-between align-items-center';
                    playerItem.innerHTML = `
                        <span>${player.playername}</span>
                        <span>Stats: ${player.playerstats}</span>
                        <button type="button" class="btn btn-primary btn-sm" onclick="addMember(${player.playerid},'${player.playername}', ${player.playerstats})">Add</button>
                    `;
                    resultsContainer.appendChild(playerItem);
                });
            });
    } else {
        document.getElementById('member-results').innerHTML = '';
    }
});

const addedPlayers = [];

function addMember(playerID, playerName, playerStats) {
    if (!addedPlayers.some(player => player.id === playerID)) {

        addedPlayers.push({ id: playerID, name: playerName, stats: playerStats });

        const playerListItem = document.createElement('li');
        playerListItem.className = 'list-group-item d-flex justify-content-between align-items-center';
        playerListItem.innerHTML = `
        <span>${playerName}</span>
        <button type="button" class="btn btn-danger btn-sm" onclick="removePlayer('${playerName}', this)">Remove</button>
    `;

        document.getElementById('added-players').appendChild(playerListItem);
        // Update team stats
        updateTeamStats();
    }
}

function removePlayer(playerName, button) {
    const index = addedPlayers.findIndex(player => player.name === playerName);
    if (index > -1) {
        addedPlayers.splice(index, 1);
        button.parentElement.remove();

        // Update team stats
        updateTeamStats();
    }
}

function updateTeamStats() {
    let totalStats = 0;
    const totalPlayer = addedPlayers.length;

    // Calculate total stats
    addedPlayers.forEach(player => {
        totalStats += player.stats;
    });

    // Calculate team stats
    const teamStats = totalPlayer === 0 ? 0 : totalStats / totalPlayer;

    // Enable the teamstats input field
    document.getElementById('teamstats').removeAttribute('disabled');

    // Update the team stats input field
    document.getElementById('teamstats').value = Math.round(teamStats);
}

function submitForm(event) {
    event.preventDefault(); // Prevent form submission

    const teamStats = parseFloat(document.getElementById('teamstats').value); // Convert to float for comparison
    const edStats = parseFloat(document.getElementById('edstats').value);

    console.log('Team Stats:', teamStats);
    console.log('Event Detail Stats:', edStats);
    // Check if the number of added players is less than
    if (addedPlayers.length < 7) {
        // Display modal if the condition is not met
        $('#minPlayerModal').modal('show');
        return;
    }

    if (teamStats < edStats) {
        // Display the modal if team stats are lower than edStats
        $('#teamStatsModal').modal('show');
        return;
    }

    // Submit the form if the condition is met

    const playerIds = addedPlayers.map(player => player.id);
    document.getElementById('player-ids').value = playerIds.join(',');
    document.getElementById('team-registration-form').submit();

}