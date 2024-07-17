export interface ScreenshotPlugin {
  take(): Promise<{ base64: string }>;
  takeO(): Promise<{ base64: string }>;
}
