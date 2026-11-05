import { mkdir, access, copyFile } from 'node:fs/promises'
import { constants } from 'node:fs'
import { dirname, resolve } from 'node:path'
import { fileURLToPath } from 'node:url'

const projectDir = resolve(dirname(fileURLToPath(import.meta.url)), '..')
const target = resolve(projectDir, 'node_modules/@dcloudio/vite-plugin-uni/lib/ssr/entry-server.js')
const template = resolve(projectDir, 'scripts/uni-entry-server.js')

try {
  await access(target, constants.F_OK)
} catch {
  await mkdir(dirname(target), { recursive: true })
  await copyFile(template, target)
}
