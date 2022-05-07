async function sendProducts(data) {
  const response = await fetch(process.env.REACT_APP_BACKEND_URL + "/Cart", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  }).then((response) => response.json());
  return response;
}

export { sendProducts };
