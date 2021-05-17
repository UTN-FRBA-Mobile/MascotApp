const fs = require('fs-extra')

try {
    fs.copySync('./src/config/config.env.example', './src/config/config.env')
    console.log('Environment file created successfully.')
} catch (err) {
    console.error(err)
}
