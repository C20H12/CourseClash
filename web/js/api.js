// package frameworks_and_drivers.DataAccess;
// StaticMethods

const CORS_PROXY_PREFIX = "https://corsproxy.io/?url=";

function getCorsSafeUrl(url) {
  let trimmed = url.trim();
  return `${CORS_PROXY_PREFIX}${trimmed}`;
}

async function Java_frameworks_and_drivers_DataAccess_StaticMethods_jsMakeRequest(lib, type, url) {
  const targetUrl = getCorsSafeUrl(url);
  console.log(targetUrl, url, typeof url);
  const requestInit = {
    method: type || "GET",
  };
  try {
    const res = await fetch(targetUrl, requestInit);
    if (res.ok) {
      const data = await res.json().catch(() => ({}));
      return JSON.stringify({ ...data, httpcode: res.status });
    }
    return JSON.stringify({ httpcode: res.status });
  } catch (err) {
    return JSON.stringify({ httpcode: 0, error: err?.message ?? String(err) });
  }
}