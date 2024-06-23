function navigateMonth(offset) {
    const params = new URLSearchParams(window.location.search);
    const currentMonth = params.get('month') ? parseInt(params.get('month')) : new Date().getMonth() + 1;
    const currentYear = params.get('year') ? parseInt(params.get('year')) : new Date().getFullYear();
    const newDate = new Date(currentYear, currentMonth - 1 + offset);

    window.location.href = `/PlayerEventCalendar?month=${newDate.getMonth() + 1}&year=${newDate.getFullYear()}`;
  }
  // Check if the displayed month matches the current system month
  document.addEventListener('DOMContentLoaded', function () {
    const params = new URLSearchParams(window.location.search);
    const currentMonth = params.get('month') ? parseInt(params.get('month')) : new Date().getMonth() + 1;

    // Get the previous button element by ID
    const previousButton = document.getElementById('previousButton');

    // Check if the displayed month is the current month
    if (currentMonth === new Date().getMonth() + 1) {
      // Hide the previous button
      previousButton.style.display = 'none';
    } else {
      // Show the previous button
      previousButton.style.display = 'block';
    }
  });

  document.addEventListener("DOMContentLoaded", function() {
    // Get today's date
    let today = new Date();
    // Format the date as YYYY-MM-DD
    let year = today.getFullYear();
    let month = (today.getMonth() + 1).toString().padStart(2, '0'); // Months are zero-based
    let day = today.getDate().toString().padStart(2, '0');
    let formattedDate = `${year}-${month}-${day}`;
    
    // Set the min attribute of the date input field
    document.getElementById('date').setAttribute('min', formattedDate);
  });