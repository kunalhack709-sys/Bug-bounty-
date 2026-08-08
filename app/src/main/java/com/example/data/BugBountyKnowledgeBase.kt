package com.example.data

object BugBountyKnowledgeBase {

    fun generateExpertResponse(userQuery: String): String {
        val query = userQuery.lowercase().trim()

        return when {
            // Web Recon & Subdomain Discovery
            containsAny(query, "recon", "subdomain", "subfinder", "httpx", "asset", "passive", "assetfinder", "amass") -> {
                """
                # 🌐 Bug Bounty Reconnaissance & Subdomain Discovery Methodology

                Effective reconnaissance is the foundation of modern bug bounty hunting. Uncovering forgotten subdomains and unmonitored assets drastically increases your attack surface.

                ---

                ### 1. Passive Subdomain Enumeration
                Gather subdomains without sending direct traffic to the target:
                ```bash
                # Using Subfinder
                subfinder -d target.com -all -o subdomains_subfinder.txt

                # Using Assetfinder
                assetfinder --subs-only target.com >> subdomains_assetfinder.txt

                # Combine & Deduplicate Results
                cat subdomains_*.txt | sort -u > all_subdomains.txt
                ```

                ---

                ### 2. Live Host Probing & Technology Detection
                Filter active HTTP/HTTPS endpoints and identify technologies running on target subdomains:
                ```bash
                cat all_subdomains.txt | httpx -title -tech-detect -status-code -content-length -follow-redirects -o live_targets.txt
                ```

                ---

                ### 3. Historical URL & Parameter Mining
                Extract historical URLs from Wayback Machine, Common Crawl, and AlienVault OTX:
                ```bash
                # Install & run waybackurls / gau
                go install github.com/tomnomnom/waybackurls@latest
                cat all_subdomains.txt | waybackurls | grep '= ' | sort -u > target_parameters.txt
                ```

                ---

                ### 🛠️ Key Pro Tips for Bug Bounty Recon:
                - **Look for 403 / 401 Bypass:** Test headers like `X-Forwarded-For: 127.0.0.1` or `X-Custom-IP-Authorization: 127.0.0.1`.
                - **JS File Scraping:** Extract hidden endpoints from bundled `.js` files using `ripgrep` or `SecretFinder`.
                - **Scope Compliance:** Always verify that discovered subdomains belong to in-scope root domains on HackerOne / Bugcrowd.
                """.trimIndent()
            }

            // OWASP Top 10 / Web Vulnerabilities / XSS / SQLi / SSRF / IDOR
            containsAny(query, "owasp", "xss", "sqli", "sql injection", "ssrf", "idor", "bola", "csrf", "cors", "lfi", "rfi") -> {
                """
                # 🛡️ OWASP Vulnerability Analysis & Hunting Guide

                Understanding common web vulnerabilities, how to identify them safely, and proving business impact is key to accepted bug bounty reports.

                ---

                ### 1. IDOR / BOLA (Insecure Direct Object Reference)
                - **Concept:** Occurs when an application exposes a reference to an internal implementation object (e.g., `GET /api/user/1042/profile`).
                - **Testing Steps:**
                  1. Create two accounts (User A and User B).
                  2. Capture User A's API request in Burp Suite / Curl.
                  3. Replace User A's user_id / account_id with User B's ID.
                  4. Verify if User B's private data is returned without authorization.

                ---

                ### 2. SSRF (Server-Side Request Forgery)
                - **Concept:** Server is tricked into fetching internal or remote resources (e.g., `GET /fetch?url=http://169.254.169.254/latest/meta-data/`).
                - **Key Targets:** Cloud metadata endpoints, internal webhook integrations, PDF generation engines.
                - **Test Payload Types:** `http://127.0.0.1:80`, `http://[::]:80`, Out-of-band DNS interactions (Burp Collaborator / interactsh).

                ---

                ### 3. Cross-Site Scripting (XSS)
                - **Reflected XSS:** Parameters reflected directly in HTML without sanitization: `<script>alert(document.domain)</script>`.
                - **Stored XSS:** User profile fields, comments, or uploaded file names saved to database and rendered to other users.
                - **DOM XSS:** Unsafe JavaScript sinks like `location.hash`, `innerHTML`, `document.write`.

                ---

                ### 4. SQL Injection (SQLi)
                - **Boolean-based:** `' OR 1=1--`
                - **Time-based blind:** `' AND SLEEP(5)--`
                - **Error-based:** `' UNION SELECT NULL, @@version--`
                """.trimIndent()
            }

            // Android / Mobile Security / JADX / Reverse Engineering
            containsAny(query, "android", "apk", "jadx", "mobile", "decompil", "frida", "objection", "sast", "manifest", "intent") -> {
                """
                # 📱 Android Application Bug Bounty & SAST Workflow

                Mobile security testing combines Static Application Security Testing (SAST) on APK files with dynamic analysis of network calls and runtime components.

                ---

                ### 1. JADX Decompilation in Termux
                Decompile APK files directly on Android using JADX CLI:
                ```bash
                # Install Java & JADX
                pkg install -y openjdk-17
                wget https://github.com/skylot/jadx/releases/download/v1.4.7/jadx-1.4.7.zip -O jadx.zip
                unzip jadx.zip -d ~/jadx
                
                # Decompile target APK
                ~/jadx/bin/jadx -d ./output_dir target_app.apk
                ```

                ---

                ### 2. Static Analysis & Secret Scanning
                Search decompiled source code for hardcoded API keys, staging URLs, and tokens:
                ```bash
                pkg install -y ripgrep
                rg -i "(api_key|secret|firebase|aws_key|token|password|auth_bearer)" ./output_dir/
                ```

                ---

                ### 3. AndroidManifest.xml Component Audit
                Check for exported activities, services, or broadcast receivers lacking permission checks:
                ```xml
                <!-- Exported Activity without permission constraint -->
                <activity android:name=".UnauthenticatedAdminActivity" android:exported="true" />
                ```
                - **Attack Vector:** An attacker app can launch exported activities or send intents to manipulate app flow (`am start -n com.target.app/.UnauthenticatedAdminActivity`).

                ---

                ### 4. Dynamic Traffic Inspection
                - Configure HTTP proxy (Burp Suite) in Wi-Fi settings.
                - Bypass SSL Pinning using Frida or Objection if required:
                ```bash
                objection --g com.target.app explore
                android sslpinning disable
                ```
                """.trimIndent()
            }

            // Fuzzing & Content Discovery (ffuf, dirsearch, gobuster)
            containsAny(query, "fuzz", "ffuf", "dirsearch", "directory", "wordlist", "gobuster", "content discovery") -> {
                """
                # ⚡ Web Fuzzing & Content Discovery Methodology

                Fuzzing helps locate hidden directories, admin panels, backup archives (`.zip`, `.bak`), and unlinked API endpoints.

                ---

                ### 1. Directory Fuzzing with ffuf
                ```bash
                # Install ffuf in Termux
                pkg install -y ffuf

                # Standard Directory Fuzzing
                ffuf -u https://target.com/FUZZ -w /path/to/wordlist.txt -mc 200,301,302,403 -sf

                # Extension Fuzzing
                ffuf -u https://target.com/FUZZ -w /path/to/wordlist.txt -e .php,.json,.bak,.zip,.config -mc 200
                ```

                ---

                ### 2. Parameter Discovery
                Discover hidden GET / POST query parameters:
                ```bash
                # Using Arjun parameter discovery
                pip install arjun
                arjun -u https://target.com/api/user -m GET
                ```

                ---

                ### 💡 Fuzzing Best Practices:
                - **Filter Noise:** Use `-fc 404` or `-fs <size>` to strip out repetitive standard 404 pages.
                - **Rate Limiting:** Use `-p 0.1` or `-rate 10` to avoid triggering IP blocks or WAF rate limits.
                - **Custom Headers:** Pass authorization tokens using `-H "Authorization: Bearer <token>"`.
                """.trimIndent()
            }

            // API Security / GraphQL / REST
            containsAny(query, "api", "graphql", "rest", "endpoint", "swagger", "json", "jwt", "bearer") -> {
                """
                # 🔌 API & GraphQL Security Bug Bounty Guide

                APIs are prime targets for logic flaws, authorization bypasses, and data exposure.

                ---

                ### 1. API Documentation & Endpoint Discovery
                Check for exposed API documentation endpoints:
                - `/swagger-ui.html`, `/v1/swagger.json`, `/api-docs`, `/v2/api-docs`
                - `/graphql`, `/graphiql`, `/api/graphql`

                ---

                ### 2. GraphQL Introspection Query
                Check if GraphQL schema introspection is enabled:
                ```bash
                curl -X POST https://target.com/graphql \
                  -H "Content-Type: application/json" \
                  -d '{"query": "{__schema{queryType{name}types{name fields{name}}}}"}'
                ```

                ---

                ### 3. Mass Assignment / Excess Data Exposure
                - Check if API returns more data than shown on the UI (e.g. `is_admin: false`, `ssn`, `internal_id`).
                - Test submitting extra parameters in PUT/POST requests:
                ```json
                {
                  "username": "user1",
                  "email": "user1@example.com",
                  "role": "admin",
                  "is_verified": true
                }
                ```

                ---

                ### 4. JWT (JSON Web Token) Security Checks
                - Test changing algorithm to `"alg": "none"`.
                - Check for weak secret key cracking using `john` or `hashcat`.
                - Check token expiration (`exp`) enforcement.
                """.trimIndent()
            }

            // Report Writing & Bug Bounty Platforms
            containsAny(query, "report", "hackerone", "bugcrowd", "intigriti", "cvss", "writeup", "bounty", "reward", "submission") -> {
                """
                # 📝 Writing Impactful Bug Bounty Reports

                A well-written report ensures fast triage, accurate CVSS scoring, and maximum bounty payout.

                ---

                ### 📋 Professional Bug Bounty Report Structure:

                #### 1. Title
                *Clear, concise, and impact-focused.*
                > **Example:** Insecure Direct Object Reference (IDOR) in `/api/v1/users/{id}/billing` allows unauthenticated access to PII.

                #### 2. Severity & CVSS
                - **Vector:** e.g., `CVSS:3.1/AV:N/AC:L/PR:N/UI:N/S:U/C:H/I:N/A:N` (High - 7.5)

                #### 3. Summary
                A brief 2-3 sentence overview of what the vulnerability is and what an attacker can achieve.

                #### 4. Step-by-Step Reproduction Steps
                1. Navigate to `https://target.com/login` and log in as standard user.
                2. Intercept request to `GET /api/v1/users/102/billing` in Burp Suite.
                3. Modify the ID from `102` to target victim ID `105`.
                4. Send the request and observe HTTP 200 response containing victim's address and last 4 digits of credit card.

                #### 5. Proof of Concept (PoC)
                Include raw HTTP Request/Response logs or curl commands:
                ```http
                GET /api/v1/users/105/billing HTTP/1.1
                Host: target.com
                Authorization: Bearer <user_token>
                ```

                #### 6. Business Impact & Remediation
                - State the exact risk to user privacy or system integrity.
                - Provide remediation guidance (e.g., implement server-side access control checks).
                """.trimIndent()
            }

            // Termux Setup / Environment
            containsAny(query, "termux", "setup", "install", "pkg", "environment", "tool", "alias", "bashrc", "battery", "wakelock") -> {
                """
                # 💻 Android Termux Environment Setup for Bug Bounty Hunting

                Transform your Android device into a mobile security research station with optimized Termux CLI tools.

                ---

                ### 1. Base Terminal Configuration
                ```bash
                # Grant storage permission & update package indices
                termux-setup-storage
                pkg update -y && pkg upgrade -y

                # Install core essential tools
                pkg install -y git python curl wget nmap net-tools jq clang tsbu ripgrep
                ```

                ---

                ### 2. Golang & Go Tools Installation
                ```bash
                pkg install -y golang

                # Configure environment paths in ~/.bashrc
                echo 'export GOPATH=${'$'}HOME/go' >> ~/.bashrc
                echo 'export PATH=${'$'}PATH:${'$'}GOPATH/bin' >> ~/.bashrc
                source ~/.bashrc

                # Install popular ProjectDiscovery tools
                go install -v github.com/projectdiscovery/subfinder/v2/cmd/subfinder@latest
                go install -v github.com/projectdiscovery/httpx/cmd/httpx@latest
                go install -v github.com/projectdiscovery/naabu/v2/cmd/naabu@latest
                ```

                ---

                ### 3. Prevent Background OS Termination
                Android battery optimization will kill long-running recon scripts. Enable Termux wakelock:
                ```bash
                termux-wake-lock
                ```
                """.trimIndent()
            }

            // Default Comprehensive Bug Bounty Methodology Overview
            else -> {
                """
                # 🎯 Bug Bounty AI Methodology & Quick Reference

                Welcome to Bug Bounty AI! Ask any question about ethical security research, vulnerability hunting, CLI tools, or mobile pentesting.

                ---

                ### 🔍 Core Bug Bounty Domains Covered:
                1. **Recon & Asset Discovery:** Subdomain enumeration (`subfinder`, `assetfinder`), live host probing (`httpx`), parameter mining (`waybackurls`, `gau`).
                2. **OWASP Web Vulnerabilities:** IDOR / BOLA, SSRF, XSS (Reflected/Stored/DOM), SQL Injection, CORS misconfigurations, CSRF, LFI/RFI.
                3. **Mobile & Android Security:** Decompiling APKs with JADX, static code analysis with `ripgrep`, AndroidManifest audit, Frida dynamic hooking.
                4. **API & GraphQL Security:** BOLA testing, GraphQL introspection, mass assignment, JWT token security.
                5. **Content Discovery & Fuzzing:** Path fuzzing with `ffuf`, parameter discovery with `arjun`.
                6. **Report Writing:** Creating clear reproduction steps, CVSS scoring, and vulnerability reports for HackerOne, Bugcrowd, and Intigriti.

                ---

                ### 💡 Try Asking:
                - *"How do I find IDOR vulnerabilities in web APIs?"*
                - *"Give me a step-by-step web recon script for Termux."*
                - *"How to decompile and search Android APKs for hidden keys?"*
                - *"What is SSRF and how do I test for cloud metadata exposure?"*
                - *"How to write a professional HackerOne bug bounty report?"*
                """.trimIndent()
            }
        }
    }

    private fun containsAny(text: String, vararg keywords: String): Boolean {
        return keywords.any { text.contains(it) }
    }
}
