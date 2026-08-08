package com.example.data

data class TermuxGuideItem(
    val title: String,
    val category: String,
    val description: String,
    val command: String,
    val tags: List<String>
)

object TermuxGuideData {
    val guides = listOf(
        TermuxGuideItem(
            title = "Termux Initial Setup & Storage",
            category = "Setup",
            description = "Grant storage access and update package repositories to prepare your Android terminal environment.",
            command = "termux-setup-storage && pkg update -y && pkg upgrade -y",
            tags = listOf("Setup", "Termux", "Packages")
        ),
        TermuxGuideItem(
            title = "Essential Recon Tools Installation",
            category = "Setup",
            description = "Install core CLI utilities: curl, wget, git, python, nmap, net-tools, and jq.",
            command = "pkg install -y git python curl wget nmap net-tools jq clang tsbu",
            tags = listOf("Setup", "Tools", "CLI")
        ),
        TermuxGuideItem(
            title = "Setting Up Go (Golang) Environment",
            category = "Setup",
            description = "Configure Go in Termux to run modern bug bounty CLI tools (subfinder, httpx, etc.).",
            command = "pkg install -y golang && echo 'export GOPATH=\$HOME/go' >> ~/.bashrc && echo 'export PATH=\$PATH:\$GOPATH/bin' >> ~/.bashrc && source ~/.bashrc",
            tags = listOf("Go", "Golang", "Setup")
        ),
        TermuxGuideItem(
            title = "Reconnaissance: Nmap Port Scanning Methodology",
            category = "Recon",
            description = "Educational port scanning syntax for discovering active network services.",
            command = "nmap -sV -sC -T4 --top-ports 100 <target_ip>",
            tags = listOf("Nmap", "Recon", "Scanning")
        ),
        TermuxGuideItem(
            title = "HTTP Header & Response Inspection",
            category = "Recon",
            description = "Examine HTTP response headers, security flags, and server software versions using curl.",
            command = "curl -I -s -L https://example.com | grep -iE 'server|x-frame-options|content-security-policy|strict-transport-security'",
            tags = listOf("HTTP", "Headers", "Curl")
        ),
        TermuxGuideItem(
            title = "Android APK Reverse Engineering Basics",
            category = "Android SAST",
            description = "Install jadx decompilation tools in Termux to inspect Android app source code.",
            command = "pkg install -y openjdk-17 && wget https://github.com/skylot/jadx/releases/download/v1.4.7/jadx-1.4.7.zip -O jadx.zip && unzip jadx.zip -d ~/jadx",
            tags = listOf("Android", "JADX", "Decompile")
        ),
        TermuxGuideItem(
            title = "APK Manifest & Hardcoded Secret Scanning",
            category = "Android SAST",
            description = "Decompile APK and search for hardcoded endpoints or API keys using ripgrep.",
            command = "pkg install -y ripgrep && rg -i '(api_key|secret|firebase|aws_key|token)' ./decompiled_apk_folder/",
            tags = listOf("Android", "Ripgrep", "Secrets")
        ),
        TermuxGuideItem(
            title = "Termux Battery & Process Optimization",
            category = "Performance",
            description = "Acquire Wakelock in Termux to prevent Android OS from killing long-running recon scripts.",
            command = "termux-wake-lock",
            tags = listOf("Performance", "Battery", "Termux")
        ),
        TermuxGuideItem(
            title = "Web Recon: Subdomain Discovery with Subfinder",
            category = "Web Recon",
            description = "Discover passive subdomains for target web applications using ProjectDiscovery subfinder.",
            command = "go install -v github.com/projectdiscovery/subfinder/v2/cmd/subfinder@latest && subfinder -d target.com -o subdomains.txt",
            tags = listOf("Web", "Subdomains", "Recon", "Go")
        ),
        TermuxGuideItem(
            title = "Web Recon: Live HTTP Host Probing with Httpx",
            category = "Web Recon",
            description = "Probe discovered subdomains for active HTTP/HTTPS web servers, status codes, and page titles.",
            command = "go install -v github.com/projectdiscovery/httpx/cmd/httpx@latest && httpx -l subdomains.txt -title -tech-detect -status-code -o live_hosts.txt",
            tags = listOf("Web", "Httpx", "HTTP", "Recon")
        ),
        TermuxGuideItem(
            title = "Web Content Discovery: Directory Fuzzing with ffuf",
            category = "Web Fuzzing",
            description = "Fuzz web application paths for hidden endpoints, backup files, and administrative panels.",
            command = "pkg install -y ffuf && ffuf -u https://target.com/FUZZ -w /path/to/wordlist.txt -mc 200,301,302,403",
            tags = listOf("Web", "Ffuf", "Fuzzing", "Directories")
        ),
        TermuxGuideItem(
            title = "Web Endpoint Mining: Wayback Machine URLs",
            category = "Web Mining",
            description = "Fetch historical web application URLs and parameters for target domain to find hidden endpoints.",
            command = "go install github.com/tomnomnom/waybackurls@latest && waybackurls target.com | grep '= ' | sort -u > params.txt",
            tags = listOf("Web", "Wayback", "Endpoints", "Params")
        ),
        TermuxGuideItem(
            title = "Web JS Analysis: Extracting Endpoints from JavaScript",
            category = "Web SAST",
            description = "Download target JavaScript bundles and extract API paths or endpoints using grep/ripgrep.",
            command = "curl -s https://target.com/app.js | grep -oE '(https?://|/api/)[a-zA-Z0-0_/-]+'",
            tags = listOf("Web", "JavaScript", "Endpoints", "API")
        ),
        TermuxGuideItem(
            title = "Detailed Website Bug Hunting Methodology",
            category = "Web Audit",
            description = "Step-by-step methodology for finding bugs in web applications: recon, endpoint mapping, authorization testing, injection attacks, and logic flaws.",
            command = "subfinder -d target.com -all | httpx -title -tech-detect | ffuf -u https://target.com/FUZZ -w wordlist.txt -mc 200,301,403",
            tags = listOf("Web", "Bug Bounty", "Methodology", "IDOR", "XSS", "SSRF")
        )
    )
}
