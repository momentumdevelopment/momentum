const axios = require("axios").default

const branch = process.argv[2]
const version = process.argv[3]
const build = process.argv[4]
const compareUrl = process.argv[5]

let description = ""

description += `**Changes:** [${branch}](${compareUrl})`
description += `\n**Download:** [Momentum v${version} Build #${build}]`

axios.post(process.env.discord_webhook, {
    username: "Momentum Beta Builds",
    avatar_url: "https://i.imgur.com/2t2ZN24.png",
    embeds: [
        {
            title: `Build #${build}`,
            description: description,
            color: 3066993
        }
    ]
})